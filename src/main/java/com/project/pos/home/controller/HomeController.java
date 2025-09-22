package com.project.pos.home.controller;

import com.project.pos.revenue.service.PaymentMethodService;
import com.project.pos.revenue.service.PaymentService;
import com.project.pos.store.dto.AmountRecordDTO;
import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.service.StoreService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
@Log4j2
public class HomeController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("/selectStore")
    public String selectStore(@RequestParam(value = "store", required = false) Long storeId, HttpSession session){
        if(storeId!=null) {
            StoreDTO storeDTO=storeService.getStoreDTOById(storeId);
            session.setAttribute("currentStore", storeDTO);
        }
        return "redirect:/home/page1";
    }

    @GetMapping("/page1")
    public void page1(HttpSession session, Model model) {
        AmountRecordDTO amountRecordDTO = (AmountRecordDTO) session.getAttribute("currentRecord");
        Boolean recordStatus = false;
        Long paymentCount=0L;
        Long cash=0L;
        Long card=0L;
        Long point=0L;

        if(amountRecordDTO!=null) {
            PosDTO posDTO = amountRecordDTO.getPosDTO();

            recordStatus=true;
            paymentCount=paymentService.getPaymentCountByPos(posDTO, amountRecordDTO.getRegDate());
            cash=paymentMethodService.getPaymentPriceByPos(posDTO, amountRecordDTO.getRegDate(), 0);
            card=paymentMethodService.getPaymentPriceByPos(posDTO, amountRecordDTO.getRegDate(), 1);
            point=paymentMethodService.getPaymentPriceByPos(posDTO, amountRecordDTO.getRegDate(), 2);
        }
        model.addAttribute("recordStatus", recordStatus);
        model.addAttribute("paymentCount", String.format("%,d", paymentCount));
        model.addAttribute("cash", String.format("%,d", cash));
        model.addAttribute("card", String.format("%,d", card));
        model.addAttribute("point", String.format("%,d", point));
        model.addAttribute("total", String.format("%,d", cash+card+point));
    }

    @GetMapping("/page2")
    public void page2(HttpSession session, Model model) {
        AmountRecordDTO amountRecordDTO = (AmountRecordDTO) session.getAttribute("currentRecord");
        Boolean recordStatus = false;
        if(amountRecordDTO!=null) recordStatus=true;
        model.addAttribute("recordStatus", recordStatus);
    }

    @GetMapping("/button-settings")
    public void button_settings() {}
}
