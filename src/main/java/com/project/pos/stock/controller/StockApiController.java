package com.project.pos.stock.controller;

import com.project.pos.option.dto.OptionDTO;
import com.project.pos.option.dto.queryDTO.OptionQuantityDTO;
import com.project.pos.option.dto.queryDTO.OptionSoldOutDTO;
import com.project.pos.option.service.OptionReceiptService;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.option.service.OptionService;
import com.project.pos.product.dto.ProductDTO;
import com.project.pos.product.dto.queryDTO.ProductQuantityDTO;
import com.project.pos.product.dto.queryDTO.ProductSoldOutDTO;
import com.project.pos.product.service.ProductReceiptService;
import com.project.pos.product.service.ProductService;
import com.project.pos.stock.dto.StockCategoryDTO;
import com.project.pos.stock.dto.StockDTO;
import com.project.pos.stock.dto.requestDTO.SaveStockDTO;
import com.project.pos.stock.dto.requestDTO.UpdateStockSoldOutDTO;
import com.project.pos.stock.dto.responseDTO.StockHistoryPageDTO;
import com.project.pos.stock.dto.responseDTO.StockPageDTO;
import com.project.pos.stock.service.StockCategoryService;
import com.project.pos.stock.service.StockMovementService;
import com.project.pos.stock.service.StockService;
import com.project.pos.store.dto.StoreDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class StockApiController {
    @Autowired
    private StockService stockService;
    @Autowired
    private OptionReceiptService optionReceiptService;
    @Autowired
    private ProductReceiptService productReceiptService;
    @Autowired
    private StockCategoryService stockCategoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private StockMovementService stockMovementService;

    @GetMapping("/api/stockPage")
    public Page<StockPageDTO> stockPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                        @RequestParam(value = "searchText", required = false) String searchText,
                                        @RequestParam(value = "searchCategory", required = false) Long searchCategory ,HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).searchText(searchText).searchCategory(searchCategory).storeDTO(storeDTO)
                .build();
        return stockService.getStockPage(requestDTO);
    }
    @GetMapping("/api/stockHistoryPage")
    public Page<StockHistoryPageDTO> stockHistoryPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                                      @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,
                                                      @RequestParam(value = "searchType") Integer searchType,
                                                      @RequestParam(value = "searchText", required = false) String searchText,
                                                      @RequestParam(value = "searchCategory", required = false) Long searchCategory , HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");

        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDate tempEnd = LocalDate.parse(endDate);
        LocalDateTime end = tempEnd.atTime(LocalTime.MAX);

        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).startDate(start).endDate(end).type(searchType)
                .searchText(searchText).searchCategory(searchCategory).storeDTO(storeDTO)
                .build();
        return stockMovementService.getStockHistoryPage(requestDTO);
    }
    @GetMapping("/api/getStock")
    public StockDTO getStock(@RequestParam(value = "id") Long id) {
        return stockService.getStockDTOById(id);
    }
    @PostMapping("/api/saveStock")
    public ResponseEntity<?> saveStock(@Valid @RequestBody SaveStockDTO saveStockDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=saveStockDTO.getId();
        Long tempNumber = stockService.countStockByNumber(storeDTO, saveStockDTO.getNumber());
        if(tempNumber!=null && !tempNumber.equals(tempId)){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("해당 번호를 사용중인 재고가 존재합니다.")));
        }
        Long cost = Long.parseLong(saveStockDTO.getStockCost().replace(",", ""));
        StockCategoryDTO stockCategoryDTO=stockCategoryService.getStockCategoryDTOById(saveStockDTO.getCategory());
        if(tempId==0L){
            StockDTO stockDTO = StockDTO.builder()
                    .id(null)
                    .number(saveStockDTO.getNumber())
                    .name(saveStockDTO.getName())
                    .stockCost(cost)
                    .unit(saveStockDTO.getUnit())
                    .quantity(0L)
                    .soldOut(true)
                    .deleted(false)
                    .storeDTO(storeDTO)
                    .stockCategoryDTO(stockCategoryDTO)
                    .build();
            stockService.createStock(stockDTO);
        }else{
            StockDTO existStockDTO = stockService.getStockDTOById(tempId);
            Long beforeCost = existStockDTO.getStockCost();
            Long difference = cost-beforeCost;

            if(difference!=0L){
                List<ProductQuantityDTO> productQuantityDTOList = productReceiptService.getProductListByStock(existStockDTO);
                if(productQuantityDTOList!=null) productQuantityDTOList.forEach(p -> productService.updateProductCost(p.getId(), p.getQuantity()*difference));
                List<OptionQuantityDTO> optionQuantityDTOList = optionReceiptService.getOptionListByStock(existStockDTO);
                if(optionQuantityDTOList!=null) optionQuantityDTOList.forEach(o -> optionService.updateOptionCost(o.getId(), o.getQuantity()*difference));
            }
            stockService.updateStock(tempId, saveStockDTO.getNumber(), saveStockDTO.getName(),
                    cost, saveStockDTO.getUnit(), stockCategoryDTO);
        }
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @DeleteMapping("/api/deleteStock")
    public ResponseEntity<?> deleteStock(@RequestParam(value = "id") Long id) {
        StockDTO stockDTO=stockService.getStockDTOById(id);
        Long optionReceiptCount = optionReceiptService.countOptionReceiptByStock(stockDTO);
        if(optionReceiptCount!=0){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("해당 재고를 사용중인 옵션이 존재합니다.")));
        }
        Long productReceiptCount = productReceiptService.countProductReceiptByStock(stockDTO);
        if(productReceiptCount!=0){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("해당 재고를 사용중인 상품이 존재합니다.")));
        }
        stockService.deleteStock(id);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @PutMapping("/api/updateStockSoldOut")
    public void updateStockSoldOut(@Valid @RequestBody UpdateStockSoldOutDTO updateStockSoldOutDTO) {
        Long tempId= updateStockSoldOutDTO.getId();
        if(tempId!=0L) {
            if(updateStockSoldOutDTO.getSoldOutType()){
                stockService.updateStockSoldOut(tempId, true);
                StockDTO stockDTO = stockService.getStockDTOById(tempId);
                List<ProductDTO> productSoldOutList = productReceiptService.getProductSoldOutList(stockDTO, false);
                if(productSoldOutList!=null){
                    for(ProductDTO temp : productSoldOutList) productService.updateProductSoldOut(temp.getId(), true);
                }
                List<OptionDTO> optionSoldOutList = optionReceiptService.getOptionSoldOutList(stockDTO, false);
                if(optionSoldOutList!=null){
                    for(OptionDTO temp : optionSoldOutList) optionService.updateOptionSoldOut(temp.getId(), true);
                }
            }else{
                stockService.updateStockSoldOut(tempId, false);
                StockDTO stockDTO = stockService.getStockDTOById(tempId);
                List<ProductSoldOutDTO> productSoldOut = productService.getProductSoldOutList(stockDTO);
                if(productSoldOut!=null){
                    for(ProductSoldOutDTO temp : productSoldOut){
                        if(temp.getSoldOutCount()==0L) productService.updateProductSoldOut(temp.getId(), false);
                    }
                }
                List<OptionSoldOutDTO> optionSoldOut = optionService.getOptionSoldOutList(stockDTO);
                if(optionSoldOut!=null){
                    for(OptionSoldOutDTO temp : optionSoldOut){
                        if(temp.getSoldOutCount()==0L) optionService.updateOptionSoldOut(temp.getId(), false);
                    }
                }
            }
        }
    }
}
