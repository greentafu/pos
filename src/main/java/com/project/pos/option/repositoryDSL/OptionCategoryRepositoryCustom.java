package com.project.pos.option.repositoryDSL;

import com.project.pos.option.dto.responseDTO.OptionCategoryPageDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface OptionCategoryRepositoryCustom {
    Page<OptionCategoryPageDTO> getOptionCategoryPage(PageRequestDTO requestDTO);
}
