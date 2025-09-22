package com.project.pos.store.service;

import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.dto.StoreTimeDTO;
import com.project.pos.store.entity.StoreTime;
import com.project.pos.store.entity.StoreTimeID;

import java.util.List;

public interface StoreTimeService {
    StoreTime getStoreTimeById(Long storeId, Long dayOfWeekId);
    StoreTimeDTO getStoreTimeDTOById(Long storeId, Long dayOfWeekId);

    StoreTimeID createStoreTime(StoreTimeDTO dto);
    StoreTimeID deleteStoreTime(Long storeId, Long dayOfWeekId);
    void updateStoreTime(Long storeKey, Long dayOfWeekKey, String startTime, String endTime, Boolean statusValue);

    StoreTime dtoToEntity(StoreTimeDTO dto);
    StoreTimeDTO entityToDto(StoreTime entity);

    List<StoreTimeDTO> getStoreTimeListByStoreDTO(StoreDTO storeDTO);
}
