package com.project.pos.stock.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.stock.dto.responseDTO.StockHistoryPageDTO;
import org.springframework.data.domain.Page;

public interface StockMovementRepositoryCustom {
    Page<StockHistoryPageDTO> getStockHistoryPage(PageRequestDTO requestDTO);
}
