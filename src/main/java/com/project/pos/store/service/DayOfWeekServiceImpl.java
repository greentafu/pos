package com.project.pos.store.service;

import com.project.pos.store.dto.DayOfWeekDTO;
import com.project.pos.store.entity.DayOfWeek;
import com.project.pos.store.repository.DayOfWeekRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DayOfWeekServiceImpl implements DayOfWeekService{
    private final DayOfWeekRepository dayOfWeekRepository;
    private final StoreService storeService;

    @Override
    public DayOfWeek getDayOfWeekById(Long id){
        return dayOfWeekRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 요일 없음"));
    }
    @Override
    public DayOfWeekDTO getDayOfWeekDTOById(Long id){
        DayOfWeek entity=getDayOfWeekById(id);
        return entityToDto(entity);
    }

    @Override
    public Long createDayOfWeek(DayOfWeekDTO dto){
        DayOfWeek entity=dtoToEntity(dto);
        DayOfWeek saved=dayOfWeekRepository.save(entity);
        return saved.getId();
    }
    @Override
    public Long deleteDayOfWeek(Long id){
        dayOfWeekRepository.deleteById(id);
        return id;
    }

    @Override
    public DayOfWeek dtoToEntity(DayOfWeekDTO dto){
        DayOfWeek entity=DayOfWeek.builder()
                .id(dto.getId())
                .dayText(dto.getDayText())
                .build();
        return entity;
    }
    @Override
    public DayOfWeekDTO entityToDto(DayOfWeek entity){
        DayOfWeekDTO dto=DayOfWeekDTO.builder()
                .id(entity.getId())
                .dayText(entity.getDayText())
                .build();
        return dto;
    }

    @Override
    public List<DayOfWeekDTO> getDayOfWeekList(){
        List<DayOfWeek> entityList = dayOfWeekRepository.findAll(Sort.by("id"));
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
}
