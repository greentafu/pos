package com.project.pos.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mail")
@RequiredArgsConstructor
@Log4j2
public class MailController {

    @GetMapping
    public void mail() {}

}
