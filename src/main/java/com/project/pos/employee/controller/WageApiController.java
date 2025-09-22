package com.project.pos.employee.controller;

import com.project.pos.employee.dto.WageDTO;
import com.project.pos.employee.dto.requestDTO.SaveWageDTO;
import com.project.pos.employee.dto.responseDTO.WagePageDTO;
import com.project.pos.employee.service.JobTitleService;
import com.project.pos.employee.service.WageService;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.StoreDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class WageApiController {
    @Autowired
    private WageService wageService;
    @Autowired
    private JobTitleService jobTitleService;

    @GetMapping("/api/wagePage")
    public Page<WagePageDTO> wagePage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                      @RequestParam(value = "searchText", required = false) String searchText, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).searchText(searchText).storeDTO(storeDTO)
                .build();
        return wageService.getWagePage(requestDTO);
    }
    @GetMapping("/api/getWage")
    public WageDTO getWage(@RequestParam(value = "id") Long id) {
        return wageService.getWageDTOById(id);
    }
    @PostMapping("/api/saveWage")
    public ResponseEntity<?> saveWage(@Valid @RequestBody SaveWageDTO saveWageDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=saveWageDTO.getId();
        String tempPerWage= saveWageDTO.getPerWage();

        WageDTO wageDTO = WageDTO.builder()
                .id(tempId==0L? null:tempId)
                .name(saveWageDTO.getName())
                .perWage(Long.parseLong(tempPerWage.replace(",", "")))
                .notes(saveWageDTO.getNotes())
                .deleted(false)
                .storeDTO(storeDTO)
                .build();
        wageService.createWage(wageDTO);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @DeleteMapping("/api/deleteWage")
    public ResponseEntity<?> deleteWage(@RequestParam(value = "id") Long id) {
        WageDTO wageDTO=wageService.getWageDTOById(id);
        Long jobTitleCount = jobTitleService.getCountJobTitleByWage(wageDTO);
        if(jobTitleCount!=0){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("해당 시급을 사용중인 직급이 존재합니다.")));
        }
        wageService.deleteWage(id);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
