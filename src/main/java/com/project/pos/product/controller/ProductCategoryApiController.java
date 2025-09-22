package com.project.pos.product.controller;

import com.project.pos.product.dto.ProductCategoryDTO;
import com.project.pos.product.dto.requestDTO.SaveCategoryArrangementDTO;
import com.project.pos.product.dto.requestDTO.SaveProductCategoryDTO;
import com.project.pos.product.dto.responseDTO.ProductCategoryArrangementDTO;
import com.project.pos.product.dto.responseDTO.ProductCategoryPageDTO;
import com.project.pos.product.service.ProductCategoryService;
import com.project.pos.product.service.ProductService;
import com.project.pos.dslDTO.PageRequestDTO;
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
public class ProductCategoryApiController {
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductService productService;

    @GetMapping("/api/productCategoryPage")
    public Page<ProductCategoryPageDTO> getProductCategoryPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                                              @RequestParam(value = "searchText", required = false) String searchText, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).searchText(searchText).storeDTO(storeDTO)
                .build();
        return productCategoryService.getProductCategoryPage(requestDTO);
    }
    @GetMapping("/api/getProductCategory")
    public ProductCategoryDTO getProductCategory(@RequestParam(value = "id") Long id) {
        return productCategoryService.getProductCategoryDTOById(id);
    }
    @PostMapping("/api/saveProductCategory")
    public ProductCategoryDTO saveProductCategory(@Valid @RequestBody SaveProductCategoryDTO saveProductCategoryDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=saveProductCategoryDTO.getId();

        if(tempId==0L){
            ProductCategoryDTO productCategoryDTO = ProductCategoryDTO.builder()
                    .id(null)
                    .name(saveProductCategoryDTO.getName())
                    .arrangement(null)
                    .deleted(false)
                    .storeDTO(storeDTO)
                    .build();
            return productCategoryService.createProductCategory(productCategoryDTO);
        }else{
            return productCategoryService.updateProductCategory(tempId, saveProductCategoryDTO.getName());
        }
    }
    @DeleteMapping("/api/deleteProductCategory")
    public ResponseEntity<?> deleteProductCategory(@RequestParam(value = "id") Long id) {
        ProductCategoryDTO productCategoryDTO=productCategoryService.getProductCategoryDTOById(id);
        Long productCount = productService.countProductByCategory(productCategoryDTO);
        if(productCount!=0){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("해당 항목을 사용중인 상품이 존재합니다.")));
        }
        productCategoryService.deleteProductCategory(id);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @GetMapping("/api/getProductCategoryList")
    public ProductCategoryArrangementDTO getProductCategoryList(HttpSession session){
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<ProductCategoryDTO> nonArray = productCategoryService.getProductCategoryNonArray(storeDTO);
        List<ProductCategoryDTO> array = productCategoryService.getProductCategoryArray(storeDTO);
        return new ProductCategoryArrangementDTO(nonArray, array);
    }
    @PostMapping("/api/saveProductCategoryArrangement")
    public ResponseEntity<?> saveProductCategoryArrangement(@Valid @RequestBody SaveCategoryArrangementDTO saveCategoryArrangementDTO, HttpSession session){
        List<Long> nonArray = saveCategoryArrangementDTO.getNonArrangementList();
        List<Long> array = saveCategoryArrangementDTO.getArrangementList();

        if(nonArray!=null) {
            nonArray.forEach(temp -> productCategoryService.updateProductCategoryArrangement(temp, null));
        }
        if(array!=null) {
            for(int i=1; i<array.size()+1; i++){
                productCategoryService.updateProductCategoryArrangement(array.get(i-1), Long.valueOf(i+""));
            }
        }
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
