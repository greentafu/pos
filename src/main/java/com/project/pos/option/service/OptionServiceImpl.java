package com.project.pos.option.service;

import com.project.pos.dslDTO.SoldOutListRequestDTO;
import com.project.pos.option.dto.queryDTO.OptionSoldOutDTO;
import com.project.pos.option.dto.queryDTO.OrderOptionDTO;
import com.project.pos.option.dto.responseDTO.OptionPageDTO;
import com.project.pos.option.dto.OptionCategoryDTO;
import com.project.pos.option.dto.OptionDTO;
import com.project.pos.option.entity.Option;
import com.project.pos.option.entity.OptionCategory;
import com.project.pos.option.repository.OptionRepository;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.stock.dto.StockDTO;
import com.project.pos.stock.entity.Stock;
import com.project.pos.stock.service.StockService;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.entity.Store;
import com.project.pos.store.service.StoreService;
import com.querydsl.core.Tuple;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptionServiceImpl implements OptionService {
    private final OptionRepository optionRepository;
    private final StoreService storeService;
    private final OptionCategoryService optionCategoryService;
    private final StockService stockService;

    @Override
    public Option getOptionById(Long id){
        return optionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 옵션 없음"));
    }
    @Override
    public OptionDTO getOptionDTOById(Long id){
        Option entity=getOptionById(id);
        return entityToDto(entity);
    }

    @Override
    public OptionDTO createOption(OptionDTO dto){
        Option entity=dtoToEntity(dto);
        Option saved=optionRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public Long deleteOption(Long id){
        Option entity = getOptionById(id);
        entity.setDeleted(true);
        return id;
    }
    @Override
    @Transactional
    public OptionDTO updateOption(Long id, Long number, String name, String displayName,
                                  Long optionPrice, Long optionCost, OptionCategoryDTO optionCategoryDTO){
        OptionCategory optionCategory = optionCategoryService.dtoToEntity(optionCategoryDTO);
        Option entity = getOptionById(id);
        entity.setNumber(number);
        entity.setName(name);
        if(displayName==null) entity.setDisplayName(name);
        else entity.setDisplayName(displayName);
        entity.setOptionPrice(optionPrice);
        entity.setOptionCost(optionCost);
        entity.setOptionCategory(optionCategory);
        return entityToDto(entity);
    }
    @Override
    @Transactional
    public Long updateOptionArrangement(Long id, Long arrangement){
        Option entity = getOptionById(id);
        entity.setArrangement(arrangement);
        return id;
    }
    @Override
    @Transactional
    public Long updateOptionSoldOut(Long id, Boolean soldOut){
        Option entity = getOptionById(id);
        entity.setSoldOut(soldOut);
        return id;
    }
    @Override
    @Transactional
    public Long updateOptionCost(Long id, Long difference){
        Option entity = getOptionById(id);
        entity.setOptionCost(entity.getOptionCost()+difference);
        return id;
    }

    @Override
    public Option dtoToEntity(OptionDTO dto){
        return Option.builder()
                .id(dto.getId())
                .number(dto.getNumber())
                .name(dto.getName())
                .displayName(dto.getDisplayName())
                .optionPrice(dto.getOptionPrice())
                .optionCost(dto.getOptionCost())
                .arrangement(dto.getArrangement())
                .soldOutType(dto.getSoldOutType())
                .soldOut(dto.getSoldOut())
                .deleted(dto.getDeleted())
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .optionCategory(dto.getOptionCategoryDTO()!=null? optionCategoryService.dtoToEntity(dto.getOptionCategoryDTO()):null)
                .build();
    }
    @Override
    public OptionDTO entityToDto(Option entity){
        return OptionDTO.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .name(entity.getName())
                .displayName(entity.getDisplayName())
                .optionPrice(entity.getOptionPrice())
                .optionCost(entity.getOptionCost())
                .arrangement(entity.getArrangement())
                .soldOutType(entity.getSoldOutType())
                .soldOut(entity.getSoldOut())
                .deleted(entity.getDeleted())
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .optionCategoryDTO(entity.getOptionCategory()!=null? optionCategoryService.entityToDto(entity.getOptionCategory()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public Long countOptionByNumber(StoreDTO storeDTO, Long number){
        Store store = storeService.dtoToEntity(storeDTO);
        return optionRepository.countOptionByNumber(store, number);
    }
    @Override
    public Long countOptionByCategory(OptionCategoryDTO dto){
        OptionCategory category = optionCategoryService.dtoToEntity(dto);
        return optionRepository.countOptionByCategory(category);
    }
    @Override
    public List<OptionDTO> getOptionNonArray(StoreDTO storeDTO, Long searchCategory){
        Store store = storeService.dtoToEntity(storeDTO);
        List<Option> entityList = optionRepository.getOptionNonArrayList(store, searchCategory);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<OptionDTO> getOptionArray(StoreDTO storeDTO, Long searchCategory){
        Store store = storeService.dtoToEntity(storeDTO);
        List<Option> entityList = optionRepository.getOptionArrayList(store, searchCategory);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<OptionSoldOutDTO> getOptionSoldOutList(StockDTO stockDTO){
        Stock stock = stockService.dtoToEntity(stockDTO);
        return optionRepository.getOptionSoldOutCountList(stock);
    }
    @Override
    public List<OptionDTO> getOptionSoldOut(SoldOutListRequestDTO requestDTO){
        List<Option> entityList = optionRepository.getSoldOutOptionList(requestDTO);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<OrderOptionDTO> getOrderOptionList(StoreDTO storeDTO){
        Store store = storeService.dtoToEntity(storeDTO);
        return optionRepository.getOrderOptionList(store);
    }
    @Override
    public Page<OptionPageDTO> getOptionPage(PageRequestDTO requestDTO){
        return optionRepository.getOptionPage(requestDTO);
    }
}
