package com.project.pos.discount.service;

import com.project.pos.discount.dto.DiscountCategoryDTO;
import com.project.pos.discount.dto.DiscountDTO;
import com.project.pos.discount.dto.queryDTO.OrderDiscountDTO;
import com.project.pos.discount.entity.Discount;
import com.project.pos.discount.entity.DiscountCategory;
import com.project.pos.discount.repository.DiscountRepository;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.discount.dto.responseDTO.DiscountPageDTO;
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
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final StoreService storeService;
    private final DiscountCategoryService discountCategoryService;

    @Override
    public Discount getDiscountById(Long id){
        return discountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 할인 없음"));
    }
    @Override
    public DiscountDTO getDiscountDTOById(Long id){
        Discount entity=getDiscountById(id);
        return entityToDto(entity);
    }

    @Override
    public Long createDiscount(DiscountDTO dto){
        Discount entity=dtoToEntity(dto);
        Discount saved=discountRepository.save(entity);
        return saved.getId();
    }
    @Override
    @Transactional
    public Long deleteDiscount(Long id){
        Discount entity = getDiscountById(id);
        entity.setDeleted(true);
        return id;
    }
    @Override
    @Transactional
    public DiscountDTO updateDiscount(Long id, Long number, String name, String displayName, Boolean discountType,
                                      Boolean typeValue, Long discountPrice, DiscountCategoryDTO discountCategoryDTO){
        Discount entity = getDiscountById(id);
        entity.setNumber(number);
        entity.setName(name);
        entity.setDisplayName(displayName);
        entity.setDiscountType(discountType);
        entity.setTypeValue(typeValue);
        entity.setDiscountPrice(discountPrice);
        entity.setDiscountCategory(discountCategoryService.dtoToEntity(discountCategoryDTO));
        return entityToDto(entity);
    }
    @Override
    @Transactional
    public Long updateDiscountArrangement(Long id, Long arrangement){
        Discount entity = getDiscountById(id);
        entity.setArrangement(arrangement);
        return id;
    }

    @Override
    public Discount dtoToEntity(DiscountDTO dto){
        return Discount.builder()
                .id(dto.getId())
                .number(dto.getNumber())
                .name(dto.getName())
                .displayName(dto.getDisplayName())
                .discountType(dto.getDiscountType())
                .typeValue(dto.getTypeValue())
                .discountPrice(dto.getDiscountPrice())
                .arrangement(dto.getArrangement())
                .deleted(dto.getDeleted())
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .discountCategory(dto.getDiscountCategoryDTO()!=null? discountCategoryService.dtoToEntity(dto.getDiscountCategoryDTO()):null)
                .build();
    }
    @Override
    public DiscountDTO entityToDto(Discount entity){
        return DiscountDTO.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .name(entity.getName())
                .displayName(entity.getDisplayName())
                .discountType(entity.getDiscountType())
                .typeValue(entity.getTypeValue())
                .discountPrice(entity.getDiscountPrice())
                .arrangement(entity.getArrangement())
                .deleted(entity.getDeleted())
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .discountCategoryDTO(entity.getDiscountCategory()!=null? discountCategoryService.entityToDto(entity.getDiscountCategory()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public Long countDiscountByCategory(DiscountCategoryDTO dto){
        DiscountCategory category = discountCategoryService.dtoToEntity(dto);
        return discountRepository.countDiscountByCategory(category);
    }
    @Override
    public List<DiscountDTO> getDiscountNonArray(StoreDTO storeDTO, Long searchCategory){
        Store store = storeService.dtoToEntity(storeDTO);
        List<Discount> entityList = discountRepository.getDiscountNonArrayList(store, searchCategory);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<DiscountDTO> getDiscountArray(StoreDTO storeDTO, Long searchCategory){
        Store store = storeService.dtoToEntity(storeDTO);
        List<Discount> entityList = discountRepository.getDiscountArrayList(store, searchCategory);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<OrderDiscountDTO> getOrderDiscountList(StoreDTO storeDTO){
        Store store = storeService.dtoToEntity(storeDTO);
        return discountRepository.getOrderDiscountList(store);
    }
    @Override
    public Page<DiscountPageDTO> getDiscountPage(PageRequestDTO requestDTO){
        return discountRepository.getDiscountPage(requestDTO);
    }
}
