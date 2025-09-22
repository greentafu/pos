package com.project.pos.discount.repositoryDSL;

import com.project.pos.discount.dto.responseDTO.DiscountCategoryPageDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface DiscountCategoryRepositoryCustom {
    Page<DiscountCategoryPageDTO> getDiscountCategoryPage(PageRequestDTO requestDTO);
}
