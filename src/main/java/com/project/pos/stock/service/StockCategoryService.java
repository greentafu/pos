package com.project.pos.stock.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.stock.dto.StockCategoryDTO;
import com.project.pos.stock.dto.responseDTO.StockCategoryPageDTO;
import com.project.pos.stock.entity.StockCategory;
import com.project.pos.store.dto.StoreDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StockCategoryService {
    StockCategory getStockCategoryById(Long id);
    StockCategoryDTO getStockCategoryDTOById(Long id);

    StockCategoryDTO createStockCategory(StockCategoryDTO dto);
    Long deleteStockCategory(Long id);
    StockCategoryDTO updateStockCategory(Long id, String name);

    StockCategory dtoToEntity(StockCategoryDTO dto);
    StockCategoryDTO entityToDto(StockCategory entity);

    List<StockCategoryDTO> getStockCategoryListByStore(StoreDTO storeDTO);
    Page<StockCategoryPageDTO> getStockCategoryPage(PageRequestDTO requestDTO);
}
