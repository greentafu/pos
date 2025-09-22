package com.project.pos.discount.controller;

import com.project.pos.discount.dto.DiscountCategoryDTO;
import com.project.pos.discount.service.DiscountCategoryService;
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
@RequestMapping("/discount")
@RequiredArgsConstructor
@Log4j2
public class DiscountController {
    @Autowired
    private DiscountCategoryService discountCategoryService;

    @GetMapping("/category-settings")
    public void category_settings() {}

    @GetMapping("/management")
    public void managements(HttpSession session, Model model) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<DiscountCategoryDTO> discountCategoryDTOList = discountCategoryService.getDiscountCategoryListByStore(storeDTO);
        model.addAttribute("discountCategoryList", discountCategoryDTOList);
    }

    @GetMapping("/button-settings")
    public void button_settings(HttpSession session, Model model) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<DiscountCategoryDTO> discountCategoryDTOList = discountCategoryService.getDiscountCategoryListByStore(storeDTO);
        model.addAttribute("discountCategoryList", discountCategoryDTOList);
    }

}
