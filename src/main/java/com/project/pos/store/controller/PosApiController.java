package com.project.pos.store.controller;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.dto.requestDTO.SavePosDTO;
import com.project.pos.store.dto.responseDTO.PosPageDTO;
import com.project.pos.store.service.PosService;
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
public class PosApiController {
    @Autowired
    private PosService posService;

    @PostMapping("/api/selectPos")
    @ResponseBody
    public void selectPos(@RequestParam("id") Long id, HttpSession session){
        PosDTO posDTO = posService.getPosDTOById(id);
        if(posDTO==null) session.removeAttribute("currentPos");
        else session.setAttribute("currentPos", posDTO);
    }
    @GetMapping("/api/posPage")
    public Page<PosPageDTO> posPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                      @RequestParam(value = "searchText", required = false) String searchText, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).searchText(searchText).storeDTO(storeDTO)
                .build();
        return posService.getPosPage(requestDTO);
    }
    @GetMapping("/api/getPos")
    public PosDTO getPos(@RequestParam(value = "id") Long id) {
        return posService.getPosDTOById(id);
    }
    @PostMapping("/api/savePos")
    public ResponseEntity<?> savePos(@Valid @RequestBody SavePosDTO savePosDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=savePosDTO.getId();
        Long tempNumber = posService.countPosByNumber(storeDTO, savePosDTO.getNumber());
        Long tempMachineID = posService.countPosByMachineId(storeDTO, savePosDTO.getMachineId());
        if(tempNumber!=null && !tempNumber.equals(tempId)){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("해당 번호를 사용중인 기기가 존재합니다.")));
        }
        if(tempMachineID!=null && !tempMachineID.equals(tempId)){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("해당 단말기식별자를 사용중인 기기가 존재합니다.")));
        }

        PosDTO posDTO = PosDTO.builder()
                .id(tempId==0L? null:tempId)
                .number(savePosDTO.getNumber())
                .name(savePosDTO.getName())
                .machineId(savePosDTO.getMachineId())
                .location(savePosDTO.getLocation())
                .status(savePosDTO.getStatus())
                .deleted(false)
                .storeDTO(storeDTO)
                .build();
        posService.createPos(posDTO);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @DeleteMapping("/api/deletePos")
    public void deletePos(@RequestParam(value = "id") Long id) {
        posService.deletePos(id);
    }
}
