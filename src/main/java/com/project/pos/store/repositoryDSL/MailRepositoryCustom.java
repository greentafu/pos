package com.project.pos.store.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.responseDTO.MailPageDTO;
import org.springframework.data.domain.Page;

public interface MailRepositoryCustom {
    Page<MailPageDTO> getMailPage(PageRequestDTO requestDTO);
}
