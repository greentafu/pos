package com.project.pos.product.controller;

import com.project.pos.product.dto.ProductCategoryDTO;
import com.project.pos.product.dto.ProductDTO;
import com.project.pos.product.dto.ProductReceiptDTO;
import com.project.pos.product.dto.ProductWithBtnDTO;
import com.project.pos.product.dto.requestDTO.ReceiptDTO;
import com.project.pos.product.dto.requestDTO.SaveProductDTO;
import com.project.pos.product.service.ProductBtnService;
import com.project.pos.product.service.ProductCategoryService;
import com.project.pos.product.service.ProductReceiptService;
import com.project.pos.product.service.ProductService;
import com.project.pos.stock.dto.StockDTO;
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
public class ProductBtnApiController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private StockService stockService;
    @Autowired
    private ProductReceiptService productReceiptService;
    @Autowired
    private ProductBtnService productBtnService;

    @GetMapping("/api/getProductBtn")
    public ProductWithBtnDTO getProduct(@RequestParam(value = "id") Long id) {
        return productBtnService.getProductWithBtn(id);
    }
    @PostMapping("/api/save22Product")
    public ResponseEntity<?> saveProduct(@Valid @RequestBody SaveProductDTO saveProductDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=saveProductDTO.getId();

        ProductCategoryDTO categoryDTO = productCategoryService.getProductCategoryDTOById(saveProductDTO.getProductCategory());

        Long tempNumber = productService.countProductByNumber(storeDTO, saveProductDTO.getNumber());
        if(tempNumber!=null && !tempNumber.equals(tempId)){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("해당 번호를 사용중인 상품이 존재합니다.")));
        }

        Long tempPrice = Long.parseLong(saveProductDTO.getProductPrice().replace(",", ""));
        Long tempCost = Long.parseLong(saveProductDTO.getProductCost().replace(",", ""));
        ProductDTO savedProductDTO=null;
        if(tempId==0L){
            ProductDTO productDTO = ProductDTO.builder()
                    .id(null)
                    .number(saveProductDTO.getNumber())
                    .name(saveProductDTO.getName())
                    .displayName(saveProductDTO.getDisplayName())
                    .productPrice(tempPrice)
                    .productCost(tempCost)
                    .soldOutType(saveProductDTO.getSoldOutType())
                    .soldOut(false)
                    .deleted(false)
                    .storeDTO(storeDTO)
                    .productCategoryDTO(categoryDTO)
                    .build();
            savedProductDTO = productService.createProduct(productDTO);
        }else{
            savedProductDTO = productService.updateProduct(tempId, saveProductDTO.getNumber(), saveProductDTO.getName(),
                    saveProductDTO.getDisplayName(), tempPrice, tempCost, categoryDTO);
            productReceiptService.deleteProductReceiptByProductKey(savedProductDTO.getId());
        }

        List<ReceiptDTO> receiptList = saveProductDTO.getReceiptList();
        for (ReceiptDTO receiptDTO : receiptList) {
            StockDTO stockDTO = stockService.getStockDTOById(receiptDTO.getId());
            if(savedProductDTO.getSoldOutType() && stockDTO.getSoldOut()) productService.updateProductSoldOut(savedProductDTO.getId(), true);

            Long quantity = Long.parseLong(receiptDTO.getQuantity().replace(",", ""));
            ProductReceiptDTO productReceiptDTO = ProductReceiptDTO.builder()
                    .productId(savedProductDTO.getId())
                    .stockId(receiptDTO.getId())
                    .quantity(quantity)
                    .stockDTO(stockDTO)
                    .productDTO(savedProductDTO)
                    .build();
            productReceiptService.createProductReceipt(productReceiptDTO);
        }
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @DeleteMapping("/api/delete22Product")
    public ResponseEntity<?> deleteProduct(@RequestParam(value = "id") Long id) {
        ProductDTO productDTO = productService.getProductDTOById(id);
        productReceiptService.deleteProductReceiptByProductKey(productDTO.getId());
        productService.deleteProduct(productDTO.getId());
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
