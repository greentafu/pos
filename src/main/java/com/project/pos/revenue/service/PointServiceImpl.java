package com.project.pos.revenue.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.revenue.dto.PaymentDTO;
import com.project.pos.revenue.dto.PaymentMethodDTO;
import com.project.pos.revenue.dto.PointDTO;
import com.project.pos.revenue.dto.responseDTO.PointPageDTO;
import com.project.pos.revenue.entity.Payment;
import com.project.pos.revenue.entity.PaymentMethod;
import com.project.pos.revenue.entity.Point;
import com.project.pos.revenue.repository.PointRepository;
import com.project.pos.store.dto.MemberDTO;
import com.project.pos.store.entity.Member;
import com.project.pos.store.service.MemberService;
import com.project.pos.store.service.StoreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
    private final PointRepository pointRepository;
    private final StoreService storeService;
    private final MemberService memberService;
    private final PaymentMethodService paymentMethodService;
    private final PaymentService paymentService;

    @Override
    public Point getPointById(Long id){
        return pointRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 포인트 없음"));
    }
    @Override
    public PointDTO getPointDTOById(Long id){
        Point entity=getPointById(id);
        return entityToDto(entity);
    }

    @Override
    public PointDTO createPoint(PointDTO dto){
        Point entity=dtoToEntity(dto);
        Point saved=pointRepository.save(entity);
        return entityToDto(entity);
    }

    @Override
    public Point dtoToEntity(PointDTO dto){
        return Point.builder()
                .id(dto.getId())
                .changingPoint(dto.getChangingPoint())
                .remainingPoint(dto.getRemainingPoint())
                .typeValue(dto.getTypeValue())
                .member(dto.getMemberDTO()!=null? memberService.dtoToEntity(dto.getMemberDTO()):null)
                .paymentMethod(dto.getPaymentMethodDTO()!=null? paymentMethodService.dtoToEntity(dto.getPaymentMethodDTO()):null)
                .payment(dto.getPaymentDTO()!=null? paymentService.dtoToEntity(dto.getPaymentDTO()):null)
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public PointDTO entityToDto(Point entity){
        return PointDTO.builder()
                .id(entity.getId())
                .changingPoint(entity.getChangingPoint())
                .remainingPoint(entity.getRemainingPoint())
                .typeValue(entity.getTypeValue())
                .memberDTO(entity.getMember()!=null? memberService.entityToDto(entity.getMember()):null)
                .paymentMethodDTO(entity.getPaymentMethod()!=null? paymentMethodService.entityToDto(entity.getPaymentMethod()):null)
                .paymentDTO(entity.getPayment()!=null? paymentService.entityToDto(entity.getPayment()):null)
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public PointDTO getPointByPaymentMethod(PaymentMethodDTO paymentMethodDTO){
        PaymentMethod paymentMethod = paymentMethodService.dtoToEntity(paymentMethodDTO);
        List<Point> entityList = pointRepository.getPointByPaymentMethod(paymentMethod);
        if(entityList.isEmpty()) return null;
        return entityToDto(entityList.get(0));
    }
    @Override
    public MemberDTO getCurrentMember(PaymentDTO paymentDTO){
        Payment payment = paymentService.dtoToEntity(paymentDTO);
        List<Member> memberList = pointRepository.getMemberByPayment(payment);
        if(memberList.isEmpty()) return null;
        return memberService.entityToDto(memberList.get(0));
    }
    @Override
    public Page<PointPageDTO> getPointPage(PageRequestDTO requestDTO){
        return pointRepository.getPointPage(requestDTO);
    }
}
