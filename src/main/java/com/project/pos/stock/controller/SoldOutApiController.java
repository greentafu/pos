package com.project.pos.stock.controller;

import com.project.pos.dslDTO.SoldOutListRequestDTO;
import com.project.pos.option.dto.OptionDTO;
import com.project.pos.option.dto.queryDTO.OptionSoldOutDTO;
import com.project.pos.option.service.OptionReceiptService;
import com.project.pos.option.service.OptionService;
import com.project.pos.product.dto.ProductDTO;
import com.project.pos.product.dto.queryDTO.ProductSoldOutDTO;
import com.project.pos.product.service.ProductReceiptService;
import com.project.pos.product.service.ProductService;
import com.project.pos.stock.dto.StockDTO;
import com.project.pos.stock.dto.requestDTO.SaveSoldOutDTO;
import com.project.pos.stock.dto.responseDTO.SoldOutDTO;
import com.project.pos.stock.dto.responseDTO.SoldOutPageDTO;
import com.project.pos.stock.service.StockService;
import com.project.pos.store.dto.StoreDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class SoldOutApiController {
    @Autowired
    private OptionService optionService;
    @Autowired
    private ProductService productService;
    @Autowired
    private StockService stockService;
    @Autowired
    private ProductReceiptService productReceiptService;
    @Autowired
    private OptionReceiptService optionReceiptService;

    @GetMapping("/api/soldOutPage")
    public SoldOutPageDTO soldOutPage (@RequestParam(value = "selectType") Long selectType,
                                       @RequestParam(value = "category") Long category ,HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        SoldOutListRequestDTO requestDTO = SoldOutListRequestDTO.builder()
                .storeDTO(storeDTO)
                .searchCategory(null)
                .soldOutType(false).build();
        if(selectType==0L){
            List<OptionDTO> nonSoldOutOption = optionService.getOptionSoldOut(requestDTO);
            List<ProductDTO> nonSoldOutProduct = productService.getProductSoldOut(requestDTO);
            List<StockDTO> nonSoldOutStock = stockService.getStockSoldOut(requestDTO);
            SoldOutDTO nonSoldOutDTO = SoldOutDTO.builder()
                    .optionList(nonSoldOutOption).productList(nonSoldOutProduct).stockList(nonSoldOutStock).build();

            requestDTO.setSoldOutType(true);
            List<OptionDTO> soldOutOption = optionService.getOptionSoldOut(requestDTO);
            List<ProductDTO> soldOutProduct = productService.getProductSoldOut(requestDTO);
            List<StockDTO> soldOutStock = stockService.getStockSoldOut(requestDTO);
            SoldOutDTO soldOutDTO = SoldOutDTO.builder()
                    .optionList(soldOutOption).productList(soldOutProduct).stockList(soldOutStock).build();

            return new SoldOutPageDTO(nonSoldOutDTO, soldOutDTO);
        }else if(selectType==1L){
            requestDTO.setSearchCategory(category);
            List<ProductDTO> nonSoldOutProduct = productService.getProductSoldOut(requestDTO);
            SoldOutDTO nonSoldOutDTO = SoldOutDTO.builder()
                    .optionList(null).productList(nonSoldOutProduct).stockList(null).build();

            requestDTO.setSoldOutType(true);
            List<ProductDTO> soldOutProduct = productService.getProductSoldOut(requestDTO);
            SoldOutDTO soldOutDTO = SoldOutDTO.builder()
                    .optionList(null).productList(soldOutProduct).stockList(null).build();

            return new SoldOutPageDTO(nonSoldOutDTO, soldOutDTO);
        }else if(selectType==2L){
            requestDTO.setSearchCategory(category);
            List<OptionDTO> nonSoldOutOption = optionService.getOptionSoldOut(requestDTO);
            SoldOutDTO nonSoldOutDTO = SoldOutDTO.builder()
                    .optionList(nonSoldOutOption).productList(null).stockList(null).build();

            requestDTO.setSoldOutType(true);
            List<OptionDTO> soldOutOption = optionService.getOptionSoldOut(requestDTO);
            SoldOutDTO soldOutDTO = SoldOutDTO.builder()
                    .optionList(soldOutOption).productList(null).stockList(null).build();

            return new SoldOutPageDTO(nonSoldOutDTO, soldOutDTO);
        }else{
            requestDTO.setSearchCategory(category);
            List<StockDTO> nonSoldOutStock = stockService.getStockSoldOut(requestDTO);
            SoldOutDTO nonSoldOutDTO = SoldOutDTO.builder()
                    .optionList(null).productList(null).stockList(nonSoldOutStock).build();

            requestDTO.setSoldOutType(true);
            List<StockDTO> soldOutStock = stockService.getStockSoldOut(requestDTO);
            SoldOutDTO soldOutDTO = SoldOutDTO.builder()
                    .optionList(null).productList(null).stockList(soldOutStock).build();

            return new SoldOutPageDTO(nonSoldOutDTO, soldOutDTO);
        }
    }
    @PutMapping("/api/saveSoldOut")
    public ResponseEntity<?> saveWage(@RequestBody SaveSoldOutDTO saveSoldOutDTO) {
        List<Long> trueOption = new ArrayList<>();
        List<Long> falseOption = new ArrayList<>();
        List<Long> trueProduct = new ArrayList<>();
        List<Long> falseProduct = new ArrayList<>();
        List<Long> trueStock = new ArrayList<>();
        List<Long> falseStock = new ArrayList<>();

        for(String temp : saveSoldOutDTO.getTrueList()){
            String text = temp.substring(0, 3);
            Long number = Long.parseLong(temp.substring(3));
            if(text.equals("opt")) trueOption.add(number);
            else if(text.equals("prd")) trueProduct.add(number);
            else trueStock.add(number);
        }
        for(String temp : saveSoldOutDTO.getFalseList()){
            String text = temp.substring(0, 3);
            Long number = Long.parseLong(temp.substring(3));
            if(text.equals("opt")) falseOption.add(number);
            else if(text.equals("prd")) falseProduct.add(number);
            else falseStock.add(number);
        }

        if(!trueOption.isEmpty()) trueOption.forEach(id -> optionService.updateOptionSoldOut(id, true));
        if(!falseOption.isEmpty()) falseOption.forEach(id -> optionService.updateOptionSoldOut(id, false));

        if(!trueProduct.isEmpty()) trueProduct.forEach(id -> productService.updateProductSoldOut(id, true));
        if(!falseProduct.isEmpty()) falseProduct.forEach(id -> productService.updateProductSoldOut(id, false));

        if(!trueStock.isEmpty()) trueStock.forEach(id -> {
            stockService.updateStockSoldOut(id, true);
            StockDTO stockDTO = stockService.getStockDTOById(id);
            List<ProductDTO> productSoldOutList = productReceiptService.getProductSoldOutList(stockDTO, false);
            if(productSoldOutList!=null){
                for(ProductDTO temp : productSoldOutList) productService.updateProductSoldOut(temp.getId(), true);
            }
            List<OptionDTO> optionSoldOutList = optionReceiptService.getOptionSoldOutList(stockDTO, false);
            if(optionSoldOutList!=null){
                for(OptionDTO temp : optionSoldOutList) optionService.updateOptionSoldOut(temp.getId(), true);
            }
        });
        if(!falseStock.isEmpty()) falseStock.forEach(id -> {
            stockService.updateStockSoldOut(id, false);
            StockDTO stockDTO = stockService.getStockDTOById(id);
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
        });

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
