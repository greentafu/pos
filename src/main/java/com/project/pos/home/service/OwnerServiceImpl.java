package com.project.pos.home.service;

import com.project.pos.home.dto.LoginDTO;
import com.project.pos.home.dto.OwnerDTO;
import com.project.pos.home.entity.Login;
import com.project.pos.home.entity.Owner;
import com.project.pos.home.repository.OwnerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnerServiceImpl implements OwnerService{
    private final OwnerRepository ownerRepository;
    private final LoginService loginService;

    @Override
    public Owner getOwnerById(Long id){
        return ownerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 사업자 없음"));
    }
    @Override
    public OwnerDTO getOwnerDTOById(Long id){
        Owner entity=getOwnerById(id);
        return entityToDto(entity);
    }

    @Override
    public Long createOwner(OwnerDTO dto){
        Owner entity=dtoToEntity(dto);
        Owner saved=ownerRepository.save(entity);
        return saved.getId();
    }
    @Override
    public Long deleteOwner(Long id){
        Owner entity=getOwnerById(id);
        entity.setDeleted(true);
        Owner deleted=ownerRepository.save(entity);
        return deleted.getId();
    }

    @Override
    public Owner dtoToEntity(OwnerDTO dto){
        Owner entity=Owner.builder()
                .id(dto.getId())
                .name(dto.getName())
                .regNumber(dto.getRegNumber())
                .ownerCode(dto.getOwnerCode())
                .storeCount(dto.getStoreCount())
                .deleted(dto.getDeleted())
                .login(dto.getLoginDTO()!=null? loginService.dtoToEntity(dto.getLoginDTO()):null)
                .build();
        return entity;
    }
    @Override
    public OwnerDTO entityToDto(Owner entity){
        OwnerDTO dto=OwnerDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .regNumber(entity.getRegNumber())
                .ownerCode(entity.getOwnerCode())
                .storeCount(entity.getStoreCount())
                .deleted(entity.getDeleted())
                .loginDTO(entity.getLogin()!=null? loginService.entityToDto(entity.getLogin()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }

    @Override
    public OwnerDTO getOwnerDTOByLogin(LoginDTO loginDTO){
        Login login=loginService.dtoToEntity(loginDTO);
        Owner entity=ownerRepository.getOwnerDTOByLogin(login);
        return entity!=null? entityToDto(entity): null;
    }
}
