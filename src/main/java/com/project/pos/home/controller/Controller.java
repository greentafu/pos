package com.project.pos.home.controller;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {

    @GetMapping({"/", "/index"})
    public String home() {
        return "redirect:/login";
    }
}
