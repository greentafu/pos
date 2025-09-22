package com.project.pos.store.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.responseDTO.MemberPageDTO;
import org.springframework.data.domain.Page;

public interface MemberRepositoryCustom {
    Page<MemberPageDTO> getMemberPage(PageRequestDTO requestDTO);
}
