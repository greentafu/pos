package com.project.pos.store.service;

import com.project.pos.employee.dto.JobTitleDTO;
import com.project.pos.store.dto.ScreenAuthorityDTO;
import com.project.pos.store.entity.ScreenAuthority;
import com.project.pos.store.entity.ScreenAuthorityID;

import java.util.List;

public interface ScreenAuthorityService {
    ScreenAuthority getScreenAuthorityById(Long jobTitleKey, Long screenKey);
    ScreenAuthorityDTO getScreenAuthorityDTOById(Long jobTitleKey, Long screenKey);

    ScreenAuthorityID createScreenAuthority(ScreenAuthorityDTO dto);
    void deleteScreenAuthorityByJobTitleKey(Long jobTitleKey);
    ScreenAuthorityID updateScreenAuthortiy(ScreenAuthorityDTO dto, Boolean authority);

    ScreenAuthority dtoToEntity(ScreenAuthorityDTO dto);
    ScreenAuthorityDTO entityToDto(ScreenAuthority entity);

    List<ScreenAuthorityDTO> getScreenAuthByJobTitle(JobTitleDTO jobTitleDTO);

}
