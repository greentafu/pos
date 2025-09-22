package com.project.pos.store.service;

import com.project.pos.store.dto.ScreenArrangementDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.entity.ScreenArrangement;
import com.project.pos.store.entity.ScreenArrangementID;

import java.util.List;

public interface ScreenArrangementService {
    ScreenArrangement getScreenArrangementById(Long storeId, Long screenId);
    ScreenArrangementDTO getScreenArrangementDTOById(Long storeId, Long screenId);

    ScreenArrangementID createScreenArrangement(ScreenArrangementDTO dto);
    ScreenArrangementID deleteScreenArrangement(Long storeId, Long screenId);
    void updateScreenArrangement(Long storeId, Long screenId, Integer page, Integer index);

    ScreenArrangement dtoToEntity(ScreenArrangementDTO dto);
    ScreenArrangementDTO entityToDto(ScreenArrangement entity);

    List<ScreenArrangementDTO> pageList(StoreDTO storeDTO, int page);
}
