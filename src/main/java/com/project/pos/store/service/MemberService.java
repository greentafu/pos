package com.project.pos.store.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.MemberDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.dto.responseDTO.MemberPageDTO;
import com.project.pos.store.entity.Member;
import org.springframework.data.domain.Page;

public interface MemberService {
    Member getMemberById(Long id);
    MemberDTO getMemberDTOById(Long id);

    MemberDTO createMember(MemberDTO dto);
    Long deleteMember(Long id);
    MemberDTO updateMember(Long id, String phoneNumber, Boolean mail);
    MemberDTO updateMemberPoint(Long id, Long point, Boolean type);
    MemberDTO updateMemberOrderFinish(Long id, Long totalPayment, Long point);

    Member dtoToEntity(MemberDTO dto);
    MemberDTO entityToDto(Member entity);

    Long getMemberByPhoneNumber(StoreDTO storeDTO, String phoneNumber);
    MemberDTO getMemberDTOByPhoneNumber(StoreDTO storeDTO, String phoneNumber);
    Page<MemberPageDTO> getMemberPage(PageRequestDTO requestDTO);
}
