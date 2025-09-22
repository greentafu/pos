package com.project.pos.home.controller;

import com.project.pos.home.dto.requestDTO.SaveHomeBtnDTO;
import com.project.pos.store.dto.ScreenArrangementDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.service.ScreenArrangementService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class HomeApiController {
    @Autowired
    private ScreenArrangementService screenArrangementService;

    @GetMapping("/api/screenArrangement")
    public ResponseEntity<List<ScreenArrangementDTO>> screenArrangement(@RequestParam int page, HttpSession session){
        StoreDTO storeDTO= (StoreDTO) session.getAttribute("currentStore");
        List<ScreenArrangementDTO> screenArrangementDTOList = screenArrangementService.pageList(storeDTO, page);
        if(screenArrangementDTOList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(screenArrangementDTOList);
    }
    @PostMapping("/api/saveHomeBtn")
    public ResponseEntity<?> saveHomeBtn(@Valid @RequestBody SaveHomeBtnDTO saveHomeBtnDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        screenArrangementService.updateScreenArrangement(storeDTO.getId(), saveHomeBtnDTO.getId(),
                saveHomeBtnDTO.getPage(), saveHomeBtnDTO.getIndex());
        return ResponseEntity.ok(Map.of("message", "Success"));
    }

}
