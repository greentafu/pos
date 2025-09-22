package com.project.pos.stock.controller;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.stock.dto.StockCategoryDTO;
import com.project.pos.stock.dto.requestDTO.SaveStockCategoryDTO;
import com.project.pos.stock.dto.responseDTO.StockCategoryPageDTO;
import com.project.pos.stock.service.StockCategoryService;
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

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class StockCategoryApiController {
    @Autowired
    private StockCategoryService stockCategoryService;
    @Autowired
    private StockService stockService;

    @GetMapping("/api/stockCategoryPage")
    public Page<StockCategoryPageDTO> getStockCategoryPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                                              @RequestParam(value = "searchText", required = false) String searchText, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).searchText(searchText).storeDTO(storeDTO)
                .build();
        return stockCategoryService.getStockCategoryPage(requestDTO);
    }
    @GetMapping("/api/getStockCategory")
    public StockCategoryDTO getStockCategory(@RequestParam(value = "id") Long id) {
        return stockCategoryService.getStockCategoryDTOById(id);
    }
    @PostMapping("/api/saveStockCategory")
    public StockCategoryDTO saveStockCategory(@Valid @RequestBody SaveStockCategoryDTO saveStockCategoryDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=saveStockCategoryDTO.getId();

        if(tempId==0L){
            StockCategoryDTO stockCategoryDTO = StockCategoryDTO.builder()
                    .id(null)
                    .name(saveStockCategoryDTO.getName())
                    .deleted(false)
                    .storeDTO(storeDTO)
                    .build();
            return stockCategoryService.createStockCategory(stockCategoryDTO);
        }else{
            return stockCategoryService.updateStockCategory(tempId, saveStockCategoryDTO.getName());
        }
    }
    @DeleteMapping("/api/deleteStockCategory")
    public ResponseEntity<?> deleteStockCategory(@RequestParam(value = "id") Long id) {
        StockCategoryDTO stockCategoryDTO=stockCategoryService.getStockCategoryDTOById(id);
        Long stockCount = stockService.countStockByCategory(stockCategoryDTO);
        if(stockCount!=0){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("해당 항목을 사용중인 재고가 존재합니다.")));
        }
        stockCategoryService.deleteStockCategory(id);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
