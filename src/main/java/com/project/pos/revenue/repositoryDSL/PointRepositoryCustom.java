package com.project.pos.revenue.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.revenue.dto.responseDTO.PointPageDTO;
import org.springframework.data.domain.Page;

public interface PointRepositoryCustom {
    Page<PointPageDTO> getPointPage(PageRequestDTO requestDTO);
}
