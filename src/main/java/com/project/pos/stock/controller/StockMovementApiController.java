package com.project.pos.stock.controller;

import com.project.pos.option.dto.OptionDTO;
import com.project.pos.option.dto.queryDTO.OptionSoldOutDTO;
import com.project.pos.option.service.OptionReceiptService;
import com.project.pos.option.service.OptionService;
import com.project.pos.product.dto.ProductDTO;
import com.project.pos.product.dto.queryDTO.ProductSoldOutDTO;
import com.project.pos.product.service.ProductReceiptService;
import com.project.pos.product.service.ProductService;
import com.project.pos.stock.dto.StockDTO;
import com.project.pos.stock.dto.StockMovementDTO;
import com.project.pos.stock.dto.requestDTO.SaveStockMovementDTO;
import com.project.pos.stock.service.StockMovementService;
import com.project.pos.stock.service.StockService;
import com.project.pos.store.dto.StoreDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class StockMovementApiController {
    @Autowired
    private StockMovementService stockMovementService;
    @Autowired
    private StockService stockService;
    @Autowired
    private ProductReceiptService productReceiptService;
    @Autowired
    private OptionReceiptService optionReceiptService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OptionService optionService;

    @PostMapping("/api/saveStockMovement")
    public ResponseEntity<?> saveStockMovement(@Valid @RequestBody SaveStockMovementDTO saveStockMovementDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long stockId=saveStockMovementDTO.getId();
        if(stockId==0L){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("수량을 변경할 재고를 선택해 주세요.")));
        }
        StockDTO stockDTO = stockService.getStockDTOById(stockId);

        Long movementAmount = Long.parseLong(saveStockMovementDTO.getMovementAmount().replace(",", ""));

        StockMovementDTO stockMovementDTO = StockMovementDTO.builder()
                .id(null)
                .movementAmount(movementAmount)
                .presentAmount(stockDTO.getQuantity()-movementAmount)
                .stockCost(stockDTO.getStockCost())
                .typeValue(saveStockMovementDTO.getTypeValue())
                .plusType(saveStockMovementDTO.getPlusType())
                .notes(saveStockMovementDTO.getNotes())
                .stockDTO(stockDTO)
                .storeDTO(storeDTO)
                .build();
        stockMovementService.createStockMovement(stockMovementDTO);

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
