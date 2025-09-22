package com.project.pos.stock.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.dslDTO.SoldOutListRequestDTO;
import com.project.pos.stock.entity.Stock;
import com.project.pos.stock.dto.responseDTO.StockPageDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StockRepositoryCustom {
    Page<StockPageDTO> getStockPage(PageRequestDTO requestDTO);
    List<Stock> getSoldOutStockList(SoldOutListRequestDTO requestDTO);
}
