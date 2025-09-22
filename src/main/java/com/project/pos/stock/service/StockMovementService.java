package com.project.pos.stock.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.stock.dto.StockMovementDTO;
import com.project.pos.stock.dto.responseDTO.StockHistoryPageDTO;
import com.project.pos.stock.entity.StockMovement;
import org.springframework.data.domain.Page;

public interface StockMovementService {
    StockMovement getStockMovementById(Long id);
    StockMovementDTO getStockMovementDTOById(Long id);

    Long createStockMovement(StockMovementDTO dto);

    StockMovement dtoToEntity(StockMovementDTO dto);
    StockMovementDTO entityToDto(StockMovement entity);

    Page<StockHistoryPageDTO> getStockHistoryPage(PageRequestDTO requestDTO);
}
