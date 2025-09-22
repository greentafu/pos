package com.project.pos.product.controller;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.product.dto.ProductBtnDTO;
import com.project.pos.product.dto.ProductCategoryDTO;
import com.project.pos.product.dto.ProductDTO;
import com.project.pos.product.dto.requestDTO.SaveMenuBtnDTO;
import com.project.pos.product.dto.requestDTO.menuBtnDTO;
import com.project.pos.product.service.ProductBtnService;
import com.project.pos.product.service.ProductCategoryService;
import com.project.pos.product.service.ProductReceiptService;
import com.project.pos.product.service.ProductService;
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

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Log4j2
public class MenuApiController {
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

    @GetMapping("/api/menuCategory")
    public Page<ProductCategoryDTO> productPage(@RequestParam(value = "page") int page, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(9).storeDTO(storeDTO)
                .build();
        return productCategoryService.getMenuCategoryPage(requestDTO);
    }
    @GetMapping("/api/getMenu")
    public List<ProductBtnDTO> getProduct(@RequestParam(value = "searchCategory") Long searchCategory) {
        return productBtnService.getProductBtnList(searchCategory);
    }
    @PostMapping("/api/saveMenuBtn")
    public ResponseEntity<?> saveMenuBtn(@Valid @RequestBody SaveMenuBtnDTO saveMenuBtnDTO) {
        Map<String, menuBtnDTO> menuMap = saveMenuBtnDTO.getMenuMap();
        Long category = saveMenuBtnDTO.getSearchCategory();

        List<Long> existKeys = productBtnService.getProductBtnIdList(category);
        List<Long> mapKeys = new ArrayList<>();

        for (Map.Entry<String, menuBtnDTO> temp : menuMap.entrySet()) {
            Long key = Long.parseLong(temp.getKey());
            menuBtnDTO menuBtnDTO = temp.getValue();

            ProductDTO productDTO = productService.getProductDTOById(key);
            mapKeys.add(productDTO.getId());

            ProductBtnDTO existBtn = productBtnService.getProductBtnByProduct(productDTO);

            if(existBtn==null){
                ProductBtnDTO productBtnDTO = ProductBtnDTO.builder()
                        .page(menuBtnDTO.getPage())
                        .indexValue(menuBtnDTO.getIndex())
                        .color(menuBtnDTO.getColor())
                        .size(menuBtnDTO.getSize())
                        .productDTO(productDTO)
                        .build();
                productBtnService.createProductBtn(productBtnDTO);
            }else{
                productBtnService.updateProductBtn(existBtn.getId(), menuBtnDTO.getPage(), menuBtnDTO.getIndex(), menuBtnDTO.getColor(), menuBtnDTO.getSize());
            }
        }

        if(existKeys!=null) {
            Set<Long> mapSet = new HashSet<>(mapKeys);
            List<Long> result = existKeys.stream().filter(id -> !mapSet.contains(id)).toList();
            result.forEach(id -> {
                ProductDTO productDTO = productService.getProductDTOById(id);
                productBtnService.deleteProductBtnByProduct(productDTO);
            });
        }

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
