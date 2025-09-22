package com.project.pos.discount.service;

import com.project.pos.discount.dto.DiscountCategoryDTO;
import com.project.pos.discount.dto.responseDTO.DiscountCategoryPageDTO;
import com.project.pos.discount.entity.DiscountCategory;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.StoreDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DiscountCategoryService {
    DiscountCategory getDiscountCategoryById(Long id);
    DiscountCategoryDTO getDiscountCategoryDTOById(Long id);

    DiscountCategoryDTO createDiscountCategory(DiscountCategoryDTO dto);
    Long deleteDiscountCategory(Long id);
    DiscountCategoryDTO updateDiscountCategory(Long id, String name, Boolean multi);
    Long updateDiscountCategoryArrangement(Long id, Long arrangement);

    DiscountCategory dtoToEntity(DiscountCategoryDTO dto);
    DiscountCategoryDTO entityToDto(DiscountCategory entity);

    List<DiscountCategoryDTO> getDiscountCategoryListByStore(StoreDTO storeDTO);
    List<DiscountCategoryDTO> getDiscountCategoryNonArray(StoreDTO storeDTO);
    List<DiscountCategoryDTO> getDiscountCategoryArray(StoreDTO storeDTO);
    Page<DiscountCategoryPageDTO> getDiscountCategoryPage(PageRequestDTO requestDTO);
}
