package com.project.pos.home.service;

import com.project.pos.home.dto.LoginDTO;
import com.project.pos.store.service.StoreService;
import com.project.pos.home.dto.LoginStoreDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.home.entity.Login;
import com.project.pos.home.entity.LoginStore;
import com.project.pos.store.entity.Store;
import com.project.pos.home.repository.LoginStoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginStoreServiceImpl implements LoginStoreService{
    private final LoginStoreRepository loginStoreRepository;
    private final LoginService loginService;
    private final StoreService storeService;

    @Override
    public LoginStore getLoginStoreById(Long id){
        return loginStoreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저-매장 없음"));
    }
    @Override
    public LoginStoreDTO getLoginStoreDTOById(Long id){
        LoginStore entity=getLoginStoreById(id);
        return entityToDto(entity);
    }

    @Override
    public Long createLoginStore(LoginStoreDTO dto){
        LoginStore entity=dtoToEntity(dto);
        LoginStore saved=loginStoreRepository.save(entity);
        return saved.getId();
    }
    @Override
    public Long deleteLoginStore(Long id){
        loginStoreRepository.deleteById(id);;
        return id;
    }

    @Override
    public LoginStore dtoToEntity(LoginStoreDTO dto){
        LoginStore entity=LoginStore.builder()
                .id(dto.getId())
                .login(dto.getLoginDTO()!=null? loginService.dtoToEntity(dto.getLoginDTO()):null)
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
        return entity;
    }
    @Override
    public LoginStoreDTO entityToDto(LoginStore entity){
        LoginStoreDTO dto=LoginStoreDTO.builder()
                .id(entity.getId())
                .loginDTO(entity.getLogin()!=null? loginService.entityToDto(entity.getLogin()):null)
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .build();
        return dto;
    }

    @Override
    public List<StoreDTO> getStoreDTOListByLogin(LoginDTO loginDTO){
        Login login=loginService.dtoToEntity(loginDTO);
        List<Store> entityList=loginStoreRepository.getStoreListByLogin(login);
        return entityList.stream().map(storeService::entityToDto).toList();
    }
}
