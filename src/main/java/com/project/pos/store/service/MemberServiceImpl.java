package com.project.pos.store.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.MemberDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.dto.responseDTO.MemberPageDTO;
import com.project.pos.store.entity.Member;
import com.project.pos.store.entity.Store;
import com.project.pos.store.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final StoreService storeService;

    @Override
    public Member getMemberById(Long id){
        return memberRepository.findById(id).orElse(null);
    }
    @Override
    public MemberDTO getMemberDTOById(Long id){
        Member entity=getMemberById(id);
        if(entity==null) return null;
        return entityToDto(entity);
    }

    @Override
    public MemberDTO createMember(MemberDTO dto){
        Member entity=dtoToEntity(dto);
        Member saved=memberRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public Long deleteMember(Long id){
        Member member=getMemberById(id);
        member.setDeleted(true);
        return id;
    }
    @Override
    @Transactional
    public MemberDTO updateMember(Long id, String phoneNumber, Boolean mail){
        Member entity = getMemberById(id);
        entity.setPhoneNumber(phoneNumber);
        entity.setMail(mail);
        return entityToDto(entity);
    }
    @Override
    @Transactional
    public MemberDTO updateMemberPoint(Long id, Long point, Boolean type){
        Member entity = getMemberById(id);
        if(type) entity.setPoints(entity.getPoints()+point);
        else entity.setPoints(entity.getPoints()-point);
        return entityToDto(entity);
    }
    @Override
    @Transactional
    public MemberDTO updateMemberOrderFinish(Long id, Long totalPayment, Long point){
        Member entity = getMemberById(id);
        entity.setTotalPayment(entity.getTotalPayment()+totalPayment);
        entity.setPoints(entity.getPoints()+point);
        return entityToDto(entity);
    }

    @Override
    public Member dtoToEntity(MemberDTO dto){
        return Member.builder()
                .id(dto.getId())
                .phoneNumber(dto.getPhoneNumber())
                .totalPayment(dto.getTotalPayment())
                .points(dto.getPoints())
                .mail(dto.getMail())
                .deleted(dto.getDeleted())
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public MemberDTO entityToDto(Member entity){
        return MemberDTO.builder()
                .id(entity.getId())
                .phoneNumber(entity.getPhoneNumber())
                .totalPayment(entity.getTotalPayment())
                .points(entity.getPoints())
                .mail(entity.getMail())
                .deleted(entity.getDeleted())
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public Long getMemberByPhoneNumber(StoreDTO storeDTO, String phoneNumber){
        Store store = storeService.dtoToEntity(storeDTO);
        return memberRepository.getMemberIdByPhoneNumber(store, phoneNumber);
    }
    @Override
    public MemberDTO getMemberDTOByPhoneNumber(StoreDTO storeDTO, String phoneNumber){
        Store store = storeService.dtoToEntity(storeDTO);
        Member entity = memberRepository.getMemberByPhoneNumber(store, phoneNumber);
        if(entity==null) return null;
        return entityToDto(entity);
    }
    @Override
    public Page<MemberPageDTO> getMemberPage(PageRequestDTO requestDTO){
        return memberRepository.getMemberPage(requestDTO);
    }
}
