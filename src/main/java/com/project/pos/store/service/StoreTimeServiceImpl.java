package com.project.pos.store.service;

import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.dto.StoreTimeDTO;
import com.project.pos.store.entity.Store;
import com.project.pos.store.entity.StoreTime;
import com.project.pos.store.entity.StoreTimeID;
import com.project.pos.store.repository.StoreTimeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreTimeServiceImpl implements StoreTimeService{
    private final StoreTimeRepository storeTimeRepository;
    private final StoreService storeService;
    private final DayOfWeekService dayOfWeekService;

    @Override
    public StoreTime getStoreTimeById(Long storeId, Long dayOfWeekId){
        return storeTimeRepository.findById(new StoreTimeID(storeId, dayOfWeekId))
                .orElseThrow(() -> new EntityNotFoundException("해당 영업시간 없음"));
    }
    @Override
    public StoreTimeDTO getStoreTimeDTOById(Long storeId, Long dayOfWeekId){
        StoreTime entity=getStoreTimeById(storeId, dayOfWeekId);
        return entityToDto(entity);
    }

    @Override
    public StoreTimeID createStoreTime(StoreTimeDTO dto){
        StoreTime entity=dtoToEntity(dto);
        StoreTime saved=storeTimeRepository.save(entity);
        return saved.getId();
    }
    @Override
    public StoreTimeID deleteStoreTime(Long storeId, Long dayOfWeekId){
        StoreTimeID id=new StoreTimeID(storeId, dayOfWeekId);
        storeTimeRepository.deleteById(id);
        return id;
    }
    @Override
    @Transactional
    public void updateStoreTime(Long storeKey, Long dayOfWeekKey, String startTime, String endTime, Boolean statusValue){
        StoreTime entity = storeTimeRepository.findById(new StoreTimeID(storeKey, dayOfWeekKey)).get();
        entity.setStartTime(startTime);
        entity.setEndTime(endTime);
        entity.setStatusValue(statusValue);
    }

    @Override
    public StoreTime dtoToEntity(StoreTimeDTO dto){
        StoreTime entity=StoreTime.builder()
                .id(new StoreTimeID(dto.getStoreId(), dto.getDayOfWeekId()))
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .statusValue(dto.getStatusValue())
                .dayOfWeek(dto.getDayOfWeekDTO()!=null? dayOfWeekService.dtoToEntity(dto.getDayOfWeekDTO()):null)
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
        return entity;
    }
    @Override
    public StoreTimeDTO entityToDto(StoreTime entity){
        StoreTimeDTO dto=StoreTimeDTO.builder()
                .storeId(entity.getId().getStoreKey())
                .dayOfWeekId(entity.getId().getDayOfWeekKey())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .statusValue(entity.getStatusValue())
                .dayOfWeekDTO(entity.getDayOfWeek()!=null? dayOfWeekService.entityToDto(entity.getDayOfWeek()):null)
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }

    @Override
    public List<StoreTimeDTO> getStoreTimeListByStoreDTO(StoreDTO storeDTO){
        Store store=storeService.dtoToEntity(storeDTO);
        List<StoreTime> entityList = storeTimeRepository.storeTimeListByStore(store);
        return entityList.stream().map(this::entityToDto).toList();
    }
}
