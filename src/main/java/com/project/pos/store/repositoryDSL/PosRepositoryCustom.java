package com.project.pos.store.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.responseDTO.PosPageDTO;
import org.springframework.data.domain.Page;

public interface PosRepositoryCustom {
    Page<PosPageDTO> getPosPage(PageRequestDTO requestDTO);
}
