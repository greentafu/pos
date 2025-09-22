package com.project.pos.revenue.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.revenue.dto.PaymentDTO;
import com.project.pos.revenue.dto.PaymentMethodDTO;
import com.project.pos.revenue.dto.PointDTO;
import com.project.pos.revenue.dto.responseDTO.PointPageDTO;
import com.project.pos.revenue.entity.Point;
import com.project.pos.store.dto.MemberDTO;
import org.springframework.data.domain.Page;

public interface PointService {
    Point getPointById(Long id);
    PointDTO getPointDTOById(Long id);

    PointDTO createPoint(PointDTO dto);

    Point dtoToEntity(PointDTO dto);
    PointDTO entityToDto(Point entity);

    PointDTO getPointByPaymentMethod(PaymentMethodDTO paymentMethodDTO);
    MemberDTO getCurrentMember(PaymentDTO paymentDTO);
    Page<PointPageDTO> getPointPage(PageRequestDTO requestDTO);
}
