package com.project.pos.revenue.controller;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.revenue.dto.PointDTO;
import com.project.pos.revenue.dto.requestDTO.UpdatePointDTO;
import com.project.pos.revenue.dto.responseDTO.PointPageDTO;
import com.project.pos.revenue.service.PointService;
import com.project.pos.store.dto.MemberDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.service.MemberService;
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
public class PointApiController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private PointService pointService;

    @GetMapping("/api/pointPage")
    public Page<PointPageDTO> wagePage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                       @RequestParam(value = "id", required = false) Long id, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).selectedId(id).storeDTO(storeDTO)
                .build();

        return pointService.getPointPage(requestDTO);
    }
    @GetMapping("/api/getPoint")
    public void getWage(@RequestParam(value = "id") Long id) {

    }
    @PostMapping("/api/savePoint")
    public ResponseEntity<?> savePoint(@Valid @RequestBody UpdatePointDTO updatePointDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=updatePointDTO.getId();
        if(tempId==0L){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("포인트를 추가할 회원을 선택해 주세요.")));
        }
        MemberDTO memberDTO=memberService.getMemberDTOById(tempId);
        Long tempChangingPoint = Long.parseLong(updatePointDTO.getChangingPoint().replace(",", ""));
        PointDTO pointDTO = PointDTO.builder()
                .id(null)
                .changingPoint(tempChangingPoint)
                .remainingPoint(memberDTO.getPoints()+tempChangingPoint)
                .typeValue(true)
                .storeDTO(storeDTO)
                .memberDTO(memberDTO)
                .build();
        pointService.createPoint(pointDTO);
        memberService.updateMemberPoint(tempId, tempChangingPoint, true);

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @PostMapping("/api/deletePoint")
    public ResponseEntity<?> deletePoint(@Valid @RequestBody UpdatePointDTO updatePointDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=updatePointDTO.getId();
        if(tempId==0L){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("포인트를 삭제할 회원을 선택해 주세요.")));
        }
        MemberDTO memberDTO=memberService.getMemberDTOById(tempId);
        Long tempChangingPoint = Long.parseLong(updatePointDTO.getChangingPoint().replace(",", ""));
        PointDTO pointDTO = PointDTO.builder()
                .id(null)
                .changingPoint(tempChangingPoint)
                .remainingPoint(memberDTO.getPoints()-tempChangingPoint)
                .typeValue(false)
                .storeDTO(storeDTO)
                .memberDTO(memberDTO)
                .build();
        pointService.createPoint(pointDTO);
        memberService.updateMemberPoint(tempId, tempChangingPoint, false);

        return ResponseEntity.ok(Map.of("message", "Success"));
    }

}
