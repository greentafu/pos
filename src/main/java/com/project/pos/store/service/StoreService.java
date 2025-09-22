package com.project.pos.store.service;

import com.project.pos.home.dto.OwnerDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.entity.Store;

public interface StoreService {
    Store getStoreById(Long id);
    StoreDTO getStoreDTOById(Long id);

    StoreDTO createStore(StoreDTO dto);
    Long deleteStore(Long id);
    Long updateStore(Long id, String name, String location, String telNumber, Long pointPercent, Boolean status);

    Store dtoToEntity(StoreDTO dto);
    StoreDTO entityToDto(Store entity);

    Long getStoreCountByOwner(OwnerDTO ownerDTO);
}
