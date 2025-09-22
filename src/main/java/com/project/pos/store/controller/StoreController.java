package com.project.pos.store.controller;

import com.project.pos.employee.dto.EmployeeDTO;
import com.project.pos.employee.service.CommuteService;
import com.project.pos.home.dto.LoginDTO;
import com.project.pos.home.dto.OwnerDTO;
import com.project.pos.home.service.LoginStoreService;
import com.project.pos.home.service.OwnerService;
import com.project.pos.revenue.service.PaymentMethodService;
import com.project.pos.store.dto.AmountRecordDTO;
import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.service.PosService;
import com.project.pos.store.service.StoreService;
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
@RequestMapping("/store")
@RequiredArgsConstructor
@Log4j2
public class StoreController {
    @Autowired
    private LoginStoreService loginStoreService;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private PosService posService;
    @Autowired
    private CommuteService commuteService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("/affiliation")
    public void affiliation(HttpSession session, Model model){
        session.removeAttribute("recordStatus");
        session.removeAttribute("currentPos");

        LoginDTO loginDTO = (LoginDTO) session.getAttribute("loginUser");
        List<StoreDTO> storeList = loginStoreService.getStoreDTOListByLogin(loginDTO);

        Boolean type=false;
        if(loginDTO.getTypeValue()){
            OwnerDTO ownerDTO=ownerService.getOwnerDTOByLogin(loginDTO);
            Long existCount = storeService.getStoreCountByOwner(ownerDTO);
            if(existCount<ownerDTO.getStoreCount()) type=true;
        }

        model.addAttribute("storeList", storeList);
        model.addAttribute("type", type);
    }
    @GetMapping("/new-store")
    public void new_store(HttpSession session, Model model){
        LoginDTO loginDTO = (LoginDTO) session.getAttribute("loginUser");
        OwnerDTO ownerDTO = ownerService.getOwnerDTOByLogin(loginDTO);
        model.addAttribute("owner", ownerDTO);
    }

    @GetMapping("/management")
    public void managements(HttpSession session, Model model){
        LoginDTO loginDTO = (LoginDTO) session.getAttribute("loginUser");
        OwnerDTO ownerDTO = ownerService.getOwnerDTOByLogin(loginDTO);
        model.addAttribute("owner", ownerDTO);
    }

    @GetMapping("/machines")
    public void machines() {}

    @GetMapping("/open")
    public void open(HttpSession session, Model model) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<PosDTO> posList = posService.getPosByStore(storeDTO);
        model.addAttribute("posList", posList);
        List<EmployeeDTO> employeeList = commuteService.getWorkingEmployeeList(storeDTO);
        model.addAttribute("employeeList", employeeList);
    }

    @GetMapping("/close")
    public void close(HttpSession session, Model model) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=(PosDTO) session.getAttribute("currentPos");
        AmountRecordDTO amountRecordDTO=(AmountRecordDTO) session.getAttribute("currentRecord");

        List<EmployeeDTO> employeeList = commuteService.getWorkingEmployeeList(storeDTO);
        model.addAttribute("employeeList", employeeList);
        model.addAttribute("pos", posDTO);
        model.addAttribute("recordId", amountRecordDTO.getId());

        Long cash = paymentMethodService.getPaymentPriceByPos(posDTO, amountRecordDTO.getRegDate(), 0)+ amountRecordDTO.getAmount();
        Long card = paymentMethodService.getPaymentPriceByPos(posDTO, amountRecordDTO.getRegDate(), 1);
        Long cancelCard = paymentMethodService.getCancelPaymentPriceByPos(posDTO, amountRecordDTO.getRegDate(), 1);
        model.addAttribute("cash", String.format("%,d", cash));
        model.addAttribute("allCard", String.format("%,d", card+cancelCard));
        model.addAttribute("card", String.format("%,d", card));
        model.addAttribute("cancelCard", String.format("%,d", cancelCard));
    }
}
