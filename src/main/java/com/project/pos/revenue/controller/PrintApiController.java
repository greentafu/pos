package com.project.pos.revenue.controller;

import com.project.pos.store.dto.StoreDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class PrintApiController {
    @PostMapping("/api/printCashReceipt")
    public ResponseEntity<?> printCashReceipt(@RequestBody List<Long> idList, HttpSession session, Model model) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        model.addAttribute("store", storeDTO);

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @PostMapping("/api/printCardReceipt")
    public ResponseEntity<?> printCardReceipt(@RequestBody List<Long> idList, HttpSession session, Model model) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        model.addAttribute("store", storeDTO);

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}