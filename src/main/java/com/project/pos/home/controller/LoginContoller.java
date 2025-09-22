package com.project.pos.home.controller;

import com.project.pos.home.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginContoller {
    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public void loginView() {}

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
