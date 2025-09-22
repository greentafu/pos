package com.project.pos.revenue.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.revenue.dto.OrdersDTO;
import com.project.pos.revenue.dto.queryDTO.*;
import com.project.pos.revenue.dto.responseDTO.ReceiptPageDTO;
import com.project.pos.revenue.entity.Orders;
import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    Orders getOrdersById(Long id);
    OrdersDTO getOrdersDTOById(Long id);

    OrdersDTO createOrders(OrdersDTO dto);
    Long deleteOrders(Long id);
    OrdersDTO updateOrders(Long id);
    OrdersDTO updateOrdersWaiting(Long id, Integer waiting);

    Orders dtoToEntity(OrdersDTO dto);
    OrdersDTO entityToDto(Orders entity);

    OrdersDTO getOrdersByStorePosWaiting(StoreDTO storeDTO, PosDTO posDTO, Integer waiting);
    Page<ReceiptPageDTO> getReceiptPage(PageRequestDTO requestDTO);
    Page<RevenueOrderPageDTO> getRevenueOrderPage(PageRequestDTO requestDTO);
    RevenueOrderSummaryDTO getRevenueOrderSummary(PageRequestDTO requestDTO);
    Page<RevenueDatePageDTO> getRevenueDatePage(PageRequestDTO requestDTO);
    RevenueDateSummaryDTO getRevenueDateSummary(PageRequestDTO requestDTO);
    Page<RevenueMenuPageDTO> getRevenueMenuPage(PageRequestDTO requestDTO);
    RevenueMenuSummaryDTO getRevenueMenuSummary(PageRequestDTO requestDTO);
    List<RevenueMenuPageDTO> getRevenueMenuChartList(PageRequestDTO requestDTO);
}
