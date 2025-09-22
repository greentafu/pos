package com.project.pos.revenue.controller;

import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.service.PosService;
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
@RequestMapping("/revenue")
@RequiredArgsConstructor
@Log4j2
public class RevenueController {
    @Autowired
    private final PosService posService;

    @GetMapping("/date-list")
    public void date_list(HttpSession session, Model model) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<PosDTO> posList = posService.getPosByStore(storeDTO);
        model.addAttribute("posList", posList);
    }

    @GetMapping("/menu-graph")
    public void menu_graph() {}

    @GetMapping("/menu-list")
    public void menu_list() {}

    @GetMapping("/order-list")
    public void order_list(HttpSession session, Model model) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        model.addAttribute("store", storeDTO);
        List<PosDTO> posList = posService.getPosByStore(storeDTO);
        model.addAttribute("posList", posList);
    }
}
