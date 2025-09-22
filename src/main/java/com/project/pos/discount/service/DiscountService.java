package com.project.pos.discount.service;

import com.project.pos.discount.dto.DiscountCategoryDTO;
import com.project.pos.discount.dto.DiscountDTO;
import com.project.pos.discount.dto.queryDTO.OrderDiscountDTO;
import com.project.pos.discount.entity.Discount;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.discount.dto.responseDTO.DiscountPageDTO;
import com.project.pos.option.dto.queryDTO.OrderOptionDTO;
import com.project.pos.store.dto.StoreDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DiscountService {
    Discount getDiscountById(Long id);
    DiscountDTO getDiscountDTOById(Long id);

    Long createDiscount(DiscountDTO dto);
    Long deleteDiscount(Long id);
    DiscountDTO updateDiscount(Long id, Long number, String name, String displayName, Boolean discountType,
                               Boolean typeValue, Long discountPrice, DiscountCategoryDTO discountCategoryDTO);
    Long updateDiscountArrangement(Long id, Long arrangement);
    
    Discount dtoToEntity(DiscountDTO dto);
    DiscountDTO entityToDto(Discount entity);

    Long countDiscountByCategory(DiscountCategoryDTO dto);
    List<DiscountDTO> getDiscountNonArray(StoreDTO storeDTO, Long searchCategory);
    List<DiscountDTO> getDiscountArray(StoreDTO storeDTO, Long searchCategory);
    List<OrderDiscountDTO> getOrderDiscountList(StoreDTO storeDTO);
    Page<DiscountPageDTO> getDiscountPage(PageRequestDTO requestDTO);
}
