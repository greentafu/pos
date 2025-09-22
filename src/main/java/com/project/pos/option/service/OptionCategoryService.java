package com.project.pos.option.service;

import com.project.pos.option.dto.OptionCategoryDTO;
import com.project.pos.option.dto.responseDTO.OptionCategoryPageDTO;
import com.project.pos.option.entity.OptionCategory;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.StoreDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OptionCategoryService {
    OptionCategory getOptionCategoryById(Long id);
    OptionCategoryDTO getOptionCategoryDTOById(Long id);

    OptionCategoryDTO createOptionCategory(OptionCategoryDTO dto);
    Long deleteOptionCategory(Long id);
    OptionCategoryDTO updateOptionCategory(Long id, String name, Boolean multi);
    Long updateOptionCategoryArrangement(Long id, Long arrangement);

    OptionCategory dtoToEntity(OptionCategoryDTO dto);
    OptionCategoryDTO entityToDto(OptionCategory entity);

    List<OptionCategoryDTO> getOptionCategoryListByStore(StoreDTO storeDTO);
    List<OptionCategoryDTO> getOptionCategoryNonArray(StoreDTO storeDTO);
    List<OptionCategoryDTO> getOptionCategoryArray(StoreDTO storeDTO);
    Page<OptionCategoryPageDTO> getOptionCategoryPage(PageRequestDTO requestDTO);
}
