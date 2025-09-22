package com.project.pos.revenue.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.revenue.dto.queryDTO.*;
import com.project.pos.revenue.dto.responseDTO.ReceiptPageDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrdersRepositoryCustom {
    Long getItemAmount(Long id);
    Long getItemDiscount(Long id);
    Long getAllBasic(Long id);
    Long getAllPercent(Long id);
    Page<ReceiptPageDTO> getReceiptPage(PageRequestDTO requestDTO);
    Page<RevenueOrderPageDTO> getRevenueOrderPage(PageRequestDTO requestDTO);
    RevenueOrderSummaryDTO getRevenueOrderSummary(PageRequestDTO requestDTO);
    Page<RevenueDatePageDTO> getRevenueDatePage(PageRequestDTO requestDTO);
    RevenueDateSummaryDTO getRevenueDateSummary(PageRequestDTO requestDTO);
    Page<RevenueMenuPageDTO> getRevenueMenuPage(PageRequestDTO requestDTO);
    RevenueMenuSummaryDTO getRevenueMenuSummary(PageRequestDTO requestDTO);
    List<RevenueMenuPageDTO> getRevenueMenuChartList(PageRequestDTO requestDTO);
}
