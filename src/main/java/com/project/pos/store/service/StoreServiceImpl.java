package com.project.pos.store.service;

import com.project.pos.home.dto.OwnerDTO;
import com.project.pos.home.entity.Owner;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.entity.Store;
import com.project.pos.store.repository.StoreRepository;
import com.project.pos.home.service.OwnerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService{
    private final StoreRepository storeRepository;
    private final OwnerService ownerService;

    @Override
    public Store getStoreById(Long id){
        return storeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 매장 없음"));
    }
    @Override
    public StoreDTO getStoreDTOById(Long id){
        Store entity=getStoreById(id);
        return entityToDto(entity);
    }

    @Override
    public StoreDTO createStore(StoreDTO dto){
        Store entity=dtoToEntity(dto);
        Store saved=storeRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    public Long deleteStore(Long id){
        Store entity=getStoreById(id);
        entity.setDeleted(true);
        Store deleted=storeRepository.save(entity);
        return deleted.getId();
    }
    @Override
    @Transactional
    public Long updateStore(Long id, String name, String location, String telNumber, Long pointPercent, Boolean status){
        Store entity = getStoreById(id);
        entity.setName(name);
        entity.setLocation(location);
        entity.setTelNumber(telNumber);
        entity.setPointPercent(pointPercent);
        entity.setStatus(status);
        return id;
    }

    @Override
    public Store dtoToEntity(StoreDTO dto){
        return Store.builder()
                .id(dto.getId())
                .name(dto.getName())
                .location(dto.getLocation())
                .telNumber(dto.getTelNumber())
                .pointPercent(dto.getPointPercent())
                .status(dto.getStatus())
                .deleted(dto.getDeleted())
                .owner(dto.getOwnerDTO()!=null? ownerService.dtoToEntity(dto.getOwnerDTO()):null)
                .build();
    }
    @Override
    public StoreDTO entityToDto(Store entity){
        return StoreDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .location(entity.getLocation())
                .telNumber(entity.getTelNumber())
                .pointPercent(entity.getPointPercent())
                .status(entity.getStatus())
                .deleted(entity.getDeleted())
                .ownerDTO(entity.getOwner()!=null? ownerService.entityToDto(entity.getOwner()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Query
    public Long getStoreCountByOwner(OwnerDTO ownerDTO){
        Owner owner=ownerService.dtoToEntity(ownerDTO);
        return storeRepository.getStoreCountByOwner(owner);
    }
}
