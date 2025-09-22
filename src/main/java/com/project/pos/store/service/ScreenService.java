package com.project.pos.store.service;

import com.project.pos.store.dto.ScreenDTO;
import com.project.pos.store.entity.Screen;

public interface ScreenService {
    Screen getScreenById(Long id);
    ScreenDTO getScreenDTOById(Long id);

    Long createScreen(ScreenDTO dto);
    Long deleteScreen(Long id);

    Screen dtoToEntity(ScreenDTO dto);
    ScreenDTO entityToDto(Screen entity);
}
