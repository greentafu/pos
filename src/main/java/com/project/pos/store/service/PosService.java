package com.project.pos.store.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.dto.responseDTO.PosPageDTO;
import com.project.pos.store.entity.Pos;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PosService {
    Pos getPosById(Long id);
    PosDTO getPosDTOById(Long id);

    Long createPos(PosDTO dto);
    Long deletePos(Long id);

    Pos dtoToEntity(PosDTO dto);
    PosDTO entityToDto(Pos entity);

    Long countPosByNumber(StoreDTO storeDTO, Long number);
    Long countPosByMachineId(StoreDTO storeDTO, String machineId);
    List<PosDTO> getPosByStore(StoreDTO storeDTO);
    Page<PosPageDTO> getPosPage(PageRequestDTO requestDTO);
}
