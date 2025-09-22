package com.project.pos.employee.repositoryDSL;

import com.project.pos.employee.dto.responseDTO.WagePageDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface WageRepositoryCustom {
    Page<WagePageDTO> getWagePage(PageRequestDTO requestDTO);
}
