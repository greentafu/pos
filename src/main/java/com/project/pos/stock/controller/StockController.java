package com.project.pos.stock.controller;

import com.project.pos.stock.dto.StockCategoryDTO;
import com.project.pos.stock.service.StockCategoryService;
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
@RequestMapping("/stock")
@RequiredArgsConstructor
@Log4j2
public class StockController {
    @Autowired
    private StockCategoryService stockCategoryService;

    @GetMapping("/category-settings")
    public void category_settings() {}

    @GetMapping("/management")
    public void managements(HttpSession session, Model model) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<StockCategoryDTO> stockCategoryDTOList = stockCategoryService.getStockCategoryListByStore(storeDTO);
        model.addAttribute("stockCategoryList", stockCategoryDTOList);
    }

    @GetMapping("/count")
    public void count(HttpSession session, Model model){
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<StockCategoryDTO> stockCategoryDTOList = stockCategoryService.getStockCategoryListByStore(storeDTO);
        model.addAttribute("stockCategoryList", stockCategoryDTOList);
    }

    @GetMapping("/history")
    public void histories(HttpSession session, Model model) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<StockCategoryDTO> stockCategoryDTOList = stockCategoryService.getStockCategoryListByStore(storeDTO);
        model.addAttribute("stockCategoryList", stockCategoryDTOList);
    }
}
