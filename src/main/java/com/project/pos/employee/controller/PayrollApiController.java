package com.project.pos.employee.controller;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.employee.dto.responseDTO.PaymentPageDTO;
import com.project.pos.employee.dto.responseDTO.PaymentSimplePageDTO;
import com.project.pos.employee.service.CommuteService;
import com.project.pos.store.dto.StoreDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequiredArgsConstructor
@Log4j2
public class PayrollApiController {
    @Autowired
    private CommuteService commuteService;

    @GetMapping("/api/paymentPage")
    public Page<PaymentPageDTO> paymentPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                            @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,
                                            @RequestParam(value = "searchCategory") Long searchCategory, @RequestParam(value = "searchText", required = false) String searchText, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");

        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDate tempEnd = LocalDate.parse(endDate);
        LocalDateTime end = tempEnd.atTime(LocalTime.MAX);

        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).startDate(start).endDate(end)
                .searchText(searchText).searchCategory(searchCategory).storeDTO(storeDTO)
                .build();
        return commuteService.getPaymentPage(requestDTO);
    }
    @GetMapping("/api/paymentSimplePage")
    public Page<PaymentSimplePageDTO> paymentSimplePage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                                        @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,
                                                        @RequestParam(value = "searchCategory") Long searchCategory, @RequestParam(value = "searchText", required = false) String searchText, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");

        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDate tempEnd = LocalDate.parse(endDate);
        LocalDateTime end = tempEnd.atTime(LocalTime.MAX);

        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).startDate(start).endDate(end)
                .searchText(searchText).searchCategory(searchCategory).storeDTO(storeDTO)
                .build();
        return commuteService.getPaymentSimplePage(requestDTO);
    }
}
