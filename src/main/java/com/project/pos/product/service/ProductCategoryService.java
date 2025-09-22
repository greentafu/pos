package com.project.pos.product.service;

import com.project.pos.product.dto.ProductCategoryDTO;
import com.project.pos.product.dto.responseDTO.ProductCategoryPageDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.product.entity.ProductCategory;
import com.project.pos.store.dto.StoreDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductCategoryService {
    ProductCategory getProductCategoryById(Long id);
    ProductCategoryDTO getProductCategoryDTOById(Long id);

    ProductCategoryDTO createProductCategory(ProductCategoryDTO dto);
    Long deleteProductCategory(Long id);
    ProductCategoryDTO updateProductCategory(Long id, String name);
    Long updateProductCategoryArrangement(Long id, Long arrangement);

    ProductCategory dtoToEntity(ProductCategoryDTO dto);
    ProductCategoryDTO entityToDto(ProductCategory entity);

    Long getFirstProductCategory(StoreDTO storeDTO);
    List<ProductCategoryDTO> getProductCategoryListByStore(StoreDTO storeDTO);
    List<ProductCategoryDTO> getProductCategoryNonArray(StoreDTO storeDTO);
    List<ProductCategoryDTO> getProductCategoryArray(StoreDTO storeDTO);
    Page<ProductCategoryPageDTO> getProductCategoryPage(PageRequestDTO requestDTO);
    Page<ProductCategoryDTO> getMenuCategoryPage(PageRequestDTO requestDTO);
}
