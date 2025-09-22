package com.project.pos.stock.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.stock.dto.responseDTO.StockCategoryPageDTO;
import org.springframework.data.domain.Page;

public interface StockCategoryRepositoryCustom {
    Page<StockCategoryPageDTO> getStockCategoryPage(PageRequestDTO requestDTO);
}
