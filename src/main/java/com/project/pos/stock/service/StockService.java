package com.project.pos.stock.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.dslDTO.SoldOutListRequestDTO;
import com.project.pos.stock.dto.StockDTO;
import com.project.pos.stock.dto.StockCategoryDTO;
import com.project.pos.stock.dto.responseDTO.StockPageDTO;
import com.project.pos.stock.entity.Stock;
import com.project.pos.store.dto.StoreDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StockService {
    Stock getStockById(Long id);
    StockDTO getStockDTOById(Long id);

    Long createStock(StockDTO dto);
    Long deleteStock(Long id);
    StockDTO updateStock(Long id, Long number, String name, Long stockCost, String unit, StockCategoryDTO stockCategoryDTO);
    StockDTO updateStockQuantity(Long id, Boolean plusType, Long number);
    StockDTO updateStockSoldOut(Long id, Boolean soldOutType);

    Stock dtoToEntity(StockDTO dto);
    StockDTO entityToDto(Stock entity);

    Long countStockByNumber(StoreDTO storeDTO, Long number);
    Long countStockByCategory(StockCategoryDTO dto);
    List<StockDTO> getStockSoldOut(SoldOutListRequestDTO requestDTO);
    Page<StockPageDTO> getStockPage(PageRequestDTO requestDTO);
}
