package com.project.pos.store.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.MailDTO;
import com.project.pos.store.dto.responseDTO.MailPageDTO;
import com.project.pos.store.entity.Mail;
import org.springframework.data.domain.Page;

public interface MailService {
    Mail getMailById(Long id);
    MailDTO getMailDTOById(Long id);

    Long createMail(MailDTO dto);
    Long deleteMail(Long id);

    Mail dtoToEntity(MailDTO dto);
    MailDTO entityToDto(Mail entity);

    Page<MailPageDTO> getMailPage(PageRequestDTO requestDTO);
}
