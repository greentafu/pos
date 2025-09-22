package com.project.pos.store.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.MailDTO;
import com.project.pos.store.dto.responseDTO.MailPageDTO;
import com.project.pos.store.entity.Mail;
import com.project.pos.store.repository.MailRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
    private final MailRepository mailRepository;
    private final StoreService storeService;

    @Override
    public Mail getMailById(Long id){
        return mailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 메일 없음"));
    }
    @Override
    public MailDTO getMailDTOById(Long id){
        Mail entity=getMailById(id);
        return entityToDto(entity);
    }

    @Override
    public Long createMail(MailDTO dto){
        Mail entity=dtoToEntity(dto);
        Mail saved=mailRepository.save(entity);
        return saved.getId();
    }
    @Override
    @Transactional
    public Long deleteMail(Long id){
        mailRepository.deleteById(id);
        return id;
    }

    @Override
    public Mail dtoToEntity(MailDTO dto){
        return Mail.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .notes(dto.getNotes())
                .content(dto.getContent())
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public MailDTO entityToDto(Mail entity){
        return MailDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .notes(entity.getNotes())
                .content(entity.getContent())
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public Page<MailPageDTO> getMailPage(PageRequestDTO requestDTO){
        return mailRepository.getMailPage(requestDTO);
    }
}
