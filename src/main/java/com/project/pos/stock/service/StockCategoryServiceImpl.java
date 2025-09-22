package com.project.pos.stock.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.stock.dto.StockCategoryDTO;
import com.project.pos.stock.dto.responseDTO.StockCategoryPageDTO;
import com.project.pos.stock.entity.StockCategory;
import com.project.pos.stock.repository.StockCategoryRepository;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.entity.Store;
import com.project.pos.store.service.StoreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockCategoryServiceImpl implements StockCategoryService {
    private final StockCategoryRepository stockCategoryRepository;
    private final StoreService storeService;

    @Override
    public StockCategory getStockCategoryById(Long id){
        return stockCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 재고항목 없음"));
    }
    @Override
    public StockCategoryDTO getStockCategoryDTOById(Long id){
        StockCategory entity=getStockCategoryById(id);
        return entityToDto(entity);
    }

    @Override
    public StockCategoryDTO createStockCategory(StockCategoryDTO dto){
        StockCategory entity=dtoToEntity(dto);
        StockCategory saved=stockCategoryRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public Long deleteStockCategory(Long id){
        StockCategory entity = getStockCategoryById(id);
        entity.setDeleted(true);
        return id;
    }
    @Override
    @Transactional
    public StockCategoryDTO updateStockCategory(Long id, String name){
        StockCategory entity = getStockCategoryById(id);
        entity.setName(name);
        return entityToDto(entity);
    }

    @Override
    public StockCategory dtoToEntity(StockCategoryDTO dto){
        return StockCategory.builder()
                .id(dto.getId())
                .name(dto.getName())
                .deleted(dto.getDeleted())
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public StockCategoryDTO entityToDto(StockCategory entity){
        return StockCategoryDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .deleted(entity.getDeleted())
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public List<StockCategoryDTO> getStockCategoryListByStore(StoreDTO storeDTO){
        Store store = storeService.dtoToEntity(storeDTO);
        List<StockCategory> entityList = stockCategoryRepository.getStockCategoryList(store);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public Page<StockCategoryPageDTO> getStockCategoryPage(PageRequestDTO requestDTO){
        return stockCategoryRepository.getStockCategoryPage(requestDTO);
    }
}
