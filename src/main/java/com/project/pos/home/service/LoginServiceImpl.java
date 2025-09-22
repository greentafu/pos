package com.project.pos.home.service;

import com.project.pos.config.PasswordUtil;
import com.project.pos.home.dto.LoginDTO;
import com.project.pos.home.entity.Login;
import com.project.pos.home.repository.LoginRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{
    private final LoginRepository loginRepository;

    @Override
    public Login getLoginById(Long id){
        return loginRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저 없음"));
    }
    @Override
    public LoginDTO getLoginDTOById(Long id){
        Login entity=getLoginById(id);
        return entityToDto(entity);
    }

    @Override
    public LoginDTO createLogin(LoginDTO dto){
        Login entity=dtoToEntity(dto);
        Login saved=loginRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public Long deleteLogin(Long id){
        Login entity=getLoginById(id);
        entity.setDeleted(true);
        return id;
    }
    @Override
    @Transactional
    public LoginDTO updateLoginPw(Long id, String pw){
        Login login=getLoginById(id);
        login.setUserPw(pw);
        return entityToDto(login);
    }

    @Override
    public Login dtoToEntity(LoginDTO dto){
        return Login.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .userPw(dto.getUserPw())
                .typeValue(dto.getTypeValue())
                .deleted(dto.getDeleted())
                .build();
    }
    @Override
    public LoginDTO entityToDto(Login entity){
        return LoginDTO.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .userPw(entity.getUserPw())
                .typeValue(entity.getTypeValue())
                .deleted(entity.getDeleted())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public LoginDTO loginProcess(String id, String pw){
        Login entity = loginRepository.findByUserId(id);
        if (entity!=null && PasswordUtil.checkPassword(pw, entity.getUserPw())) return entityToDto(entity);
        return null;
    }
}
