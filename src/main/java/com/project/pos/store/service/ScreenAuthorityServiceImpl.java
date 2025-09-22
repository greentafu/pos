package com.project.pos.store.service;

import com.project.pos.employee.dto.JobTitleDTO;
import com.project.pos.employee.entity.JobTitle;
import com.project.pos.employee.service.JobTitleService;
import com.project.pos.store.dto.ScreenAuthorityDTO;
import com.project.pos.store.entity.ScreenAuthority;
import com.project.pos.store.entity.ScreenAuthorityID;
import com.project.pos.store.repository.ScreenAuthorityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreenAuthorityServiceImpl implements ScreenAuthorityService{
    private final ScreenAuthorityRepository screenAuthorityRepository;
    private final ScreenService screenService;
    private final JobTitleService jobTitleService;

    @Override
    public ScreenAuthority getScreenAuthorityById(Long jobTitleKey, Long screenKey){
        return screenAuthorityRepository.findById(new ScreenAuthorityID(jobTitleKey, screenKey))
                .orElse(null);
    }
    @Override
    public ScreenAuthorityDTO getScreenAuthorityDTOById(Long jobTitleKey, Long screenKey){
        ScreenAuthority entity=getScreenAuthorityById(jobTitleKey, screenKey);
        return entity!=null? entityToDto(entity):null;
    }

    @Override
    public ScreenAuthorityID createScreenAuthority(ScreenAuthorityDTO dto){
        ScreenAuthority entity=dtoToEntity(dto);
        ScreenAuthority saved=screenAuthorityRepository.save(entity);
        return saved.getId();
    }
    @Override
    public void deleteScreenAuthorityByJobTitleKey(Long jobTitleKey){
        screenAuthorityRepository.deleteScreenAuthorityByJobTitleKey(jobTitleKey);
    }
    @Override
    @Transactional
    public ScreenAuthorityID updateScreenAuthortiy(ScreenAuthorityDTO dto, Boolean authority){
        ScreenAuthority entity = getScreenAuthorityById(dto.getJobTitleId(), dto.getScreenId());
        entity.setAuthority(authority);
        return entity.getId();
    }

    @Override
    public ScreenAuthority dtoToEntity(ScreenAuthorityDTO dto){
        return ScreenAuthority.builder()
                .id(ScreenAuthorityID.builder().jobTitleKey(dto.getJobTitleId()).screenKey(dto.getScreenId()).build())
                .authority(dto.getAuthority())
                .jobTitle(dto.getJobTitleDTO()!=null? jobTitleService.dtoToEntity(dto.getJobTitleDTO()):null)
                .screen(dto.getScreenDTO()!=null? screenService.dtoToEntity(dto.getScreenDTO()):null)
                .build();
    }
    @Override
    public ScreenAuthorityDTO entityToDto(ScreenAuthority entity){
        return ScreenAuthorityDTO.builder()
                .jobTitleId(entity.getJobTitle().getId())
                .screenId(entity.getScreen().getId())
                .authority(entity.getAuthority())
                .jobTitleDTO(entity.getJobTitle()!=null? jobTitleService.entityToDto(entity.getJobTitle()):null)
                .screenDTO(entity.getScreen()!=null? screenService.entityToDto(entity.getScreen()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public List<ScreenAuthorityDTO> getScreenAuthByJobTitle(JobTitleDTO jobTitleDTO){
        JobTitle jobTitle = jobTitleService.dtoToEntity(jobTitleDTO);
        List<ScreenAuthority> entityList = screenAuthorityRepository.screenAuthorityList(jobTitle);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
}
