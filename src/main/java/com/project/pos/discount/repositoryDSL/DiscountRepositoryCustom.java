package com.project.pos.discount.repositoryDSL;

import com.project.pos.discount.dto.queryDTO.OrderDiscountDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.discount.dto.responseDTO.DiscountPageDTO;
import com.project.pos.store.entity.Store;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DiscountRepositoryCustom {
    Page<DiscountPageDTO> getDiscountPage(PageRequestDTO requestDTO);
    List<OrderDiscountDTO> getOrderDiscountList(Store store);
}
