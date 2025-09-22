package com.project.pos.product.controller;

import com.project.pos.product.dto.ProductCategoryDTO;
import com.project.pos.product.service.ProductCategoryService;
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
@RequestMapping("/product")
@RequiredArgsConstructor
@Log4j2
public class ProductController {
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private StockCategoryService stockCategoryService;

    @GetMapping("/category-settings")
    public void category_settings() {}

    @GetMapping("/management")
    public void managements(HttpSession session, Model model) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<ProductCategoryDTO> productCategoryDTOList = productCategoryService.getProductCategoryListByStore(storeDTO);
        List<StockCategoryDTO> stockCategoryDTOList = stockCategoryService.getStockCategoryListByStore(storeDTO);

        model.addAttribute("productCategoryList", productCategoryDTOList);
        model.addAttribute("stockCategoryList", stockCategoryDTOList);
    }

    @GetMapping("/button-settings")
    public void button_settings(HttpSession session, Model model) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<ProductCategoryDTO> productCategoryDTOList = productCategoryService.getProductCategoryArray(storeDTO);
        model.addAttribute("productCategoryList", productCategoryDTOList);
        model.addAttribute("firstCategory", productCategoryService.getFirstProductCategory(storeDTO));
    }

}
