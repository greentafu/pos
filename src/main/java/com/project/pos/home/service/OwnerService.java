package com.project.pos.home.service;

import com.project.pos.home.dto.LoginDTO;
import com.project.pos.home.dto.OwnerDTO;
import com.project.pos.home.entity.Owner;

public interface OwnerService {
    Owner getOwnerById(Long id);
    OwnerDTO getOwnerDTOById(Long id);

    Long createOwner(OwnerDTO dto);
    Long deleteOwner(Long id);

    Owner dtoToEntity(OwnerDTO dto);
    OwnerDTO entityToDto(Owner entity);

    OwnerDTO getOwnerDTOByLogin(LoginDTO loginDTO);
}
