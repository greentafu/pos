package com.project.pos.home.service;

import com.project.pos.home.dto.LoginDTO;
import com.project.pos.home.dto.LoginStoreDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.home.entity.LoginStore;

import java.util.List;

public interface LoginStoreService {
    LoginStore getLoginStoreById(Long id);
    LoginStoreDTO getLoginStoreDTOById(Long id);

    Long createLoginStore(LoginStoreDTO dto);
    Long deleteLoginStore(Long id);

    LoginStore dtoToEntity(LoginStoreDTO dto);
    LoginStoreDTO entityToDto(LoginStore entity);

    List<StoreDTO> getStoreDTOListByLogin(LoginDTO loginDTO);
}
