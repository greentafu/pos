package com.project.pos.stock.controller;

import com.project.pos.option.dto.OptionCategoryDTO;
import com.project.pos.option.service.OptionCategoryService;
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
@RequestMapping("/sold-out")
@RequiredArgsConstructor
@Log4j2
public class SoldOutController {
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private OptionCategoryService optionCategoryService;
    @Autowired
    private StockCategoryService stockCategoryService;

    @GetMapping
    public void sold_out(HttpSession session, Model model) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");

        List<ProductCategoryDTO> productCategoryDTOList = productCategoryService.getProductCategoryListByStore(storeDTO);
        List<OptionCategoryDTO> optionCategoryDTOList = optionCategoryService.getOptionCategoryListByStore(storeDTO);
        List<StockCategoryDTO> stockCategoryDTOList = stockCategoryService.getStockCategoryListByStore(storeDTO);

        model.addAttribute("productCategoryList", productCategoryDTOList);
        model.addAttribute("optionCategoryList", optionCategoryDTOList);
        model.addAttribute("stockCategoryList", stockCategoryDTOList);
    }
}
