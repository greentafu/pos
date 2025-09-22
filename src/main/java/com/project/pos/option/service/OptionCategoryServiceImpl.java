package com.project.pos.option.service;

import com.project.pos.option.dto.OptionCategoryDTO;
import com.project.pos.option.dto.responseDTO.OptionCategoryPageDTO;
import com.project.pos.option.entity.OptionCategory;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.entity.Store;
import com.project.pos.store.service.StoreService;
import com.project.pos.option.repository.OptionCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionCategoryServiceImpl implements OptionCategoryService {
    private final OptionCategoryRepository optionCategoryRepository;
    private final StoreService storeService;

    @Override
    public OptionCategory getOptionCategoryById(Long id){
        return optionCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 옵션항목 없음"));
    }
    @Override
    public OptionCategoryDTO getOptionCategoryDTOById(Long id){
        OptionCategory entity=getOptionCategoryById(id);
        return entityToDto(entity);
    }

    @Override
    public OptionCategoryDTO createOptionCategory(OptionCategoryDTO dto){
        OptionCategory entity=dtoToEntity(dto);
        OptionCategory saved=optionCategoryRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public Long deleteOptionCategory(Long id){
        OptionCategory entity = getOptionCategoryById(id);
        entity.setDeleted(true);
        return id;
    }
    @Override
    @Transactional
    public OptionCategoryDTO updateOptionCategory(Long id, String name, Boolean multi){
        OptionCategory entity = getOptionCategoryById(id);
        entity.setName(name);
        entity.setMulti(multi);
        return entityToDto(entity);
    }
    @Override
    @Transactional
    public Long updateOptionCategoryArrangement(Long id, Long arrangement){
        OptionCategory entity = getOptionCategoryById(id);
        entity.setArrangement(arrangement);
        return id;
    }

    @Override
    public OptionCategory dtoToEntity(OptionCategoryDTO dto){
        return OptionCategory.builder()
                .id(dto.getId())
                .name(dto.getName())
                .arrangement(dto.getArrangement())
                .multi(dto.getMulti())
                .deleted(dto.getDeleted())
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public OptionCategoryDTO entityToDto(OptionCategory entity){
        return OptionCategoryDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .arrangement(entity.getArrangement())
                .multi(entity.getMulti())
                .deleted(entity.getDeleted())
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public List<OptionCategoryDTO> getOptionCategoryListByStore(StoreDTO storeDTO){
        Store store = storeService.dtoToEntity(storeDTO);
        List<OptionCategory> entityList = optionCategoryRepository.getOptionCategoryListByStore(store);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<OptionCategoryDTO> getOptionCategoryNonArray(StoreDTO storeDTO){
        Store store = storeService.dtoToEntity(storeDTO);
        List<OptionCategory> entityList = optionCategoryRepository.getOptionCategoryNonArrayList(store);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<OptionCategoryDTO> getOptionCategoryArray(StoreDTO storeDTO){
        Store store = storeService.dtoToEntity(storeDTO);
        List<OptionCategory> entityList = optionCategoryRepository.getOptionCategoryArrayList(store);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public Page<OptionCategoryPageDTO> getOptionCategoryPage(PageRequestDTO requestDTO){
        return optionCategoryRepository.getOptionCategoryPage(requestDTO);
    }
}
