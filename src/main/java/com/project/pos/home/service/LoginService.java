package com.project.pos.home.service;

import com.project.pos.home.dto.LoginDTO;
import com.project.pos.home.entity.Login;

public interface LoginService {
    Login getLoginById(Long id);
    LoginDTO getLoginDTOById(Long id);

    LoginDTO createLogin(LoginDTO dto);
    Long deleteLogin(Long id);
    LoginDTO updateLoginPw(Long id, String pw);

    Login dtoToEntity(LoginDTO dto);
    LoginDTO entityToDto(Login entity);

    LoginDTO loginProcess(String id, String pw);
}
