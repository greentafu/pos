package com.project.pos.employee.controller;

import com.project.pos.employee.dto.JobTitleDTO;
import com.project.pos.employee.dto.WageDTO;
import com.project.pos.employee.service.JobTitleService;
import com.project.pos.employee.service.WageService;
import com.project.pos.store.dto.StoreDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
@Log4j2
public class EmployeeController {
    @Autowired
    private WageService wageService;
    @Autowired
    private JobTitleService jobTitleService;

    @GetMapping("/management")
    public void managements(Model model) {
        List<JobTitleDTO> jobTitleDTOList = jobTitleService.getJobTitleList();
        model.addAttribute("jobTitleList", jobTitleDTOList);
    }

    @GetMapping("/payment")
    public void payment(Model model, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<WageDTO> wageDTOList = wageService.getWageList(storeDTO);
        model.addAttribute("wageList", wageDTOList);
    }

    @GetMapping("/position")
    public void position(Model model, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<WageDTO> wageDTOList = wageService.getWageList(storeDTO);
        model.addAttribute("wageList", wageDTOList);
    }

    @GetMapping("/wage")
    public void wage() {}

    @GetMapping("/commute")
    public void commute() {}
}
