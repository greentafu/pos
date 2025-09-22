package com.project.pos.store.controller;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.MemberDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.dto.requestDTO.SaveMemberDTO;
import com.project.pos.store.dto.responseDTO.MemberPageDTO;
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
public class MemberApiController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/api/memberPage")
    public Page<MemberPageDTO> memberPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                          @RequestParam(value = "searchText", required = false) String searchText, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).searchText(searchText).storeDTO(storeDTO)
                .build();
        return memberService.getMemberPage(requestDTO);
    }
    @GetMapping("/api/getMember")
    public MemberDTO getMember(@RequestParam(value = "id") Long id) {
        return memberService.getMemberDTOById(id);
    }
    @GetMapping("/api/getMemberByPhone")
    public MemberDTO getMemberByPhone(@RequestParam(value = "phoneNumber") String phoneNumber, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        return memberService.getMemberDTOByPhoneNumber(storeDTO, phoneNumber);
    }
    @PostMapping("/api/saveMember")
    public ResponseEntity<?> saveMember(@Valid @RequestBody SaveMemberDTO saveMemberDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=saveMemberDTO.getId();
        Long tempMemberId = memberService.getMemberByPhoneNumber(storeDTO, saveMemberDTO.getPhoneNumber());
        if(tempMemberId!=null && !tempMemberId.equals(tempId)){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("해당 번호를 사용중인 회원이 존재합니다.")));
        }

        MemberDTO result=null;
        if(tempId==0L){
            MemberDTO memberDTO = MemberDTO.builder()
                    .id(null)
                    .phoneNumber(saveMemberDTO.getPhoneNumber())
                    .totalPayment(0L)
                    .points(0L)
                    .mail(saveMemberDTO.getMail())
                    .deleted(false)
                    .storeDTO(storeDTO)
                    .build();
            result=memberService.createMember(memberDTO);
        }else{
            result=memberService.updateMember(tempId, saveMemberDTO.getPhoneNumber(), saveMemberDTO.getMail());
        }
        return ResponseEntity.ok(result);
    }
    @DeleteMapping("/api/deleteMember")
    public ResponseEntity<?> deleteMember(@RequestParam(value = "id") Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
