package com.project.pos.home.controller;

import com.project.pos.home.dto.LoginDTO;
import com.project.pos.home.dto.responseDTO.LoginProcessDTO;
import com.project.pos.home.service.LoginService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class LoginApiController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/api/login")
    public ResponseEntity<?> loginProcess(@Valid @RequestBody LoginProcessDTO loginProcessDTO, HttpSession session) {
        LoginDTO dto=loginService.loginProcess(loginProcessDTO.getUserId(), loginProcessDTO.getUserPw());
        if (dto==null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("아이디와 로그인이 올바르지 않습니다.")));
        }
        session.setAttribute("loginUser", dto);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
