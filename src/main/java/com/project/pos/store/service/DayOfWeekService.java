package com.project.pos.store.service;

import com.project.pos.store.dto.DayOfWeekDTO;
import com.project.pos.store.entity.DayOfWeek;

import java.util.List;

public interface DayOfWeekService {
    DayOfWeek getDayOfWeekById(Long id);
    DayOfWeekDTO getDayOfWeekDTOById(Long id);

    Long createDayOfWeek(DayOfWeekDTO dto);
    Long deleteDayOfWeek(Long id);

    DayOfWeek dtoToEntity(DayOfWeekDTO dto);
    DayOfWeekDTO entityToDto(DayOfWeek entity);

    List<DayOfWeekDTO> getDayOfWeekList();
}
