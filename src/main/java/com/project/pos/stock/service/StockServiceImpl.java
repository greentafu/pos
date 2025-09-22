package com.project.pos.stock.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.dslDTO.SoldOutListRequestDTO;
import com.project.pos.stock.dto.StockDTO;
import com.project.pos.stock.entity.Stock;
import com.project.pos.stock.dto.StockCategoryDTO;
import com.project.pos.stock.dto.responseDTO.StockPageDTO;
import com.project.pos.stock.entity.StockCategory;
import com.project.pos.stock.repository.StockRepository;
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
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final StoreService storeService;
    private final StockCategoryService stockCategoryService;

    @Override
    public Stock getStockById(Long id){
        return stockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 재고 없음"));
    }
    @Override
    public StockDTO getStockDTOById(Long id){
        Stock entity=getStockById(id);
        return entityToDto(entity);
    }

    @Override
    public Long createStock(StockDTO dto){
        Stock entity=dtoToEntity(dto);
        Stock saved=stockRepository.save(entity);
        return saved.getId();
    }
    @Override
    @Transactional
    public Long deleteStock(Long id){
        Stock entity = getStockById(id);
        entity.setDeleted(true);
        return id;
    }
    @Override
    @Transactional
    public StockDTO updateStock(Long id, Long number, String name, Long stockCost, String unit, StockCategoryDTO stockCategoryDTO){
        Stock entity = getStockById(id);
        entity.setNumber(number);
        entity.setName(name);
        entity.setStockCost(stockCost);
        entity.setUnit(unit);
        entity.setStockCategory(stockCategoryService.dtoToEntity(stockCategoryDTO));
        return entityToDto(entity);
    }
    @Override
    @Transactional
    public StockDTO updateStockQuantity(Long id, Boolean plusType, Long number){
        Stock entity = getStockById(id);
        if(plusType) {
            entity.setQuantity(entity.getQuantity()+number);
            entity.setSoldOut(false);
        }
        else {
            Long temp = entity.getQuantity()-number;
            entity.setQuantity(temp);
            entity.setSoldOut(false);
        }
        return entityToDto(entity);
    }
    @Override
    @Transactional
    public StockDTO updateStockSoldOut(Long id, Boolean soldOutType){
        Stock entity = getStockById(id);
        entity.setSoldOut(soldOutType);
        return entityToDto(entity);
    }

    @Override
    public Stock dtoToEntity(StockDTO dto){
        return Stock.builder()
                .id(dto.getId())
                .number(dto.getNumber())
                .name(dto.getName())
                .stockCost(dto.getStockCost())
                .unit(dto.getUnit())
                .quantity(dto.getQuantity())
                .soldOut(dto.getSoldOut())
                .deleted(dto.getDeleted())
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .stockCategory(dto.getStockCategoryDTO()!=null? stockCategoryService.dtoToEntity(dto.getStockCategoryDTO()):null)
                .build();
    }
    @Override
    public StockDTO entityToDto(Stock entity){
        return StockDTO.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .name(entity.getName())
                .stockCost(entity.getStockCost())
                .unit(entity.getUnit())
                .quantity(entity.getQuantity())
                .soldOut(entity.getSoldOut())
                .deleted(entity.getDeleted())
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .stockCategoryDTO(entity.getStockCategory()!=null? stockCategoryService.entityToDto(entity.getStockCategory()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public Long countStockByNumber(StoreDTO storeDTO, Long number){
        Store store=storeService.dtoToEntity(storeDTO);
        return stockRepository.countStockByNumber(store, number);
    }
    @Override
    public Long countStockByCategory(StockCategoryDTO dto){
        StockCategory category = stockCategoryService.dtoToEntity(dto);
        return stockRepository.countStockByCategory(category);
    }
    @Override
    public List<StockDTO> getStockSoldOut(SoldOutListRequestDTO requestDTO){
        List<Stock> entityList = stockRepository.getSoldOutStockList(requestDTO);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public Page<StockPageDTO> getStockPage(PageRequestDTO requestDTO){
        return stockRepository.getStockPage(requestDTO);
    }
}
