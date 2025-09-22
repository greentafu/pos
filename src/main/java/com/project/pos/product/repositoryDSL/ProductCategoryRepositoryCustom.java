package com.project.pos.product.repositoryDSL;

import com.project.pos.product.dto.responseDTO.ProductCategoryPageDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.product.entity.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductCategoryRepositoryCustom {
    Page<ProductCategoryPageDTO> getProductCategoryPage(PageRequestDTO requestDTO);

    Page<ProductCategory> getMenuCategoryPage(PageRequestDTO requestDTO);
}
