package com.project.pos.product.service;

import com.project.pos.product.dto.ProductCategoryDTO;
import com.project.pos.product.entity.ProductCategory;
import com.project.pos.product.dto.responseDTO.ProductCategoryPageDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.entity.Store;
import com.project.pos.store.service.StoreService;
import com.project.pos.product.repository.ProductCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final StoreService storeService;

    @Override
    public ProductCategory getProductCategoryById(Long id){
        return productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품항목 없음"));
    }
    @Override
    public ProductCategoryDTO getProductCategoryDTOById(Long id){
        ProductCategory entity=getProductCategoryById(id);
        return entityToDto(entity);
    }

    @Override
    public ProductCategoryDTO createProductCategory(ProductCategoryDTO dto){
        ProductCategory entity=dtoToEntity(dto);
        ProductCategory saved=productCategoryRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public Long deleteProductCategory(Long id){
        ProductCategory entity = getProductCategoryById(id);
        entity.setDeleted(true);
        return id;
    }
    @Override
    @Transactional
    public ProductCategoryDTO updateProductCategory(Long id, String name){
        ProductCategory entity = getProductCategoryById(id);
        entity.setName(name);
        return entityToDto(entity);
    }
    @Override
    @Transactional
    public Long updateProductCategoryArrangement(Long id, Long arrangement){
        ProductCategory entity = getProductCategoryById(id);
        entity.setArrangement(arrangement);
        return id;
    }

    @Override
    public ProductCategory dtoToEntity(ProductCategoryDTO dto){
        return ProductCategory.builder()
                .id(dto.getId())
                .name(dto.getName())
                .arrangement(dto.getArrangement())
                .deleted(dto.getDeleted())
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public ProductCategoryDTO entityToDto(ProductCategory entity){
        return ProductCategoryDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .arrangement(entity.getArrangement())
                .deleted(entity.getDeleted())
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public Long getFirstProductCategory(StoreDTO storeDTO){
        Store store = storeService.dtoToEntity(storeDTO);
        Optional<Long> temp = productCategoryRepository.getFirstProductCategory(store);
        return temp.orElse(null);
    }
    @Override
    public List<ProductCategoryDTO> getProductCategoryListByStore(StoreDTO storeDTO){
        Store store = storeService.dtoToEntity(storeDTO);
        List<ProductCategory> entityList = productCategoryRepository.getProductCategoryListByStore(store);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<ProductCategoryDTO> getProductCategoryNonArray(StoreDTO storeDTO){
        Store store = storeService.dtoToEntity(storeDTO);
        List<ProductCategory> entityList = productCategoryRepository.getProductCategoryNonArrayList(store);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<ProductCategoryDTO> getProductCategoryArray(StoreDTO storeDTO){
        Store store = storeService.dtoToEntity(storeDTO);
        List<ProductCategory> entityList = productCategoryRepository.getProductCategoryArrayList(store);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public Page<ProductCategoryPageDTO> getProductCategoryPage(PageRequestDTO requestDTO){
        return productCategoryRepository.getProductCategoryPage(requestDTO);
    }
    @Override
    public Page<ProductCategoryDTO> getMenuCategoryPage(PageRequestDTO requestDTO){
        Page<ProductCategory> entityPage = productCategoryRepository.getMenuCategoryPage(requestDTO);
        return entityPage.map(this::entityToDto);
    }
}
