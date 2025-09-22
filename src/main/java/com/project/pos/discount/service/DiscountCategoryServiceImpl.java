package com.project.pos.discount.service;

import com.project.pos.discount.dto.responseDTO.DiscountCategoryPageDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.discount.dto.DiscountCategoryDTO;
import com.project.pos.discount.entity.DiscountCategory;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.entity.Store;
import com.project.pos.store.service.StoreService;
import com.project.pos.discount.repository.DiscountCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountCategoryServiceImpl implements DiscountCategoryService {
    private final DiscountCategoryRepository discountCategoryRepository;
    private final StoreService storeService;

    @Override
    public DiscountCategory getDiscountCategoryById(Long id){
        return discountCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 할인항목 없음"));
    }
    @Override
    public DiscountCategoryDTO getDiscountCategoryDTOById(Long id){
        DiscountCategory entity=getDiscountCategoryById(id);
        return entityToDto(entity);
    }

    @Override
    public DiscountCategoryDTO  createDiscountCategory(DiscountCategoryDTO dto){
        DiscountCategory entity=dtoToEntity(dto);
        DiscountCategory saved=discountCategoryRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public Long deleteDiscountCategory(Long id){
        DiscountCategory entity = getDiscountCategoryById(id);
        entity.setDeleted(true);
        return id;
    }
    @Override
    @Transactional
    public DiscountCategoryDTO  updateDiscountCategory(Long id, String name, Boolean multi){
        DiscountCategory entity = getDiscountCategoryById(id);
        entity.setName(name);
        entity.setMulti(multi);
        return entityToDto(entity);
    }
    @Override
    @Transactional
    public Long updateDiscountCategoryArrangement(Long id, Long arrangement){
        DiscountCategory entity = getDiscountCategoryById(id);
        entity.setArrangement(arrangement);
        return id;
    }

    @Override
    public DiscountCategory dtoToEntity(DiscountCategoryDTO dto){
        return DiscountCategory.builder()
                .id(dto.getId())
                .name(dto.getName())
                .arrangement(dto.getArrangement())
                .multi(dto.getMulti())
                .deleted(dto.getDeleted())
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public DiscountCategoryDTO entityToDto(DiscountCategory entity){
        return DiscountCategoryDTO.builder()
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
    public List<DiscountCategoryDTO> getDiscountCategoryListByStore(StoreDTO storeDTO){
        Store store = storeService.dtoToEntity(storeDTO);
        List<DiscountCategory> entityList = discountCategoryRepository.getDiscountCategoryList(store);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<DiscountCategoryDTO> getDiscountCategoryNonArray(StoreDTO storeDTO){
        Store store = storeService.dtoToEntity(storeDTO);
        List<DiscountCategory> entityList = discountCategoryRepository.getDiscountCategoryNonArrayList(store);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<DiscountCategoryDTO> getDiscountCategoryArray(StoreDTO storeDTO){
        Store store = storeService.dtoToEntity(storeDTO);
        List<DiscountCategory> entityList = discountCategoryRepository.getDiscountCategoryArrayList(store);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public Page<DiscountCategoryPageDTO> getDiscountCategoryPage(PageRequestDTO requestDTO){
        return discountCategoryRepository.getDiscountCategoryPage(requestDTO);
    }
}
