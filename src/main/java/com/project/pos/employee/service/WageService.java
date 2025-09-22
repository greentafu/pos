package com.project.pos.employee.service;

import com.project.pos.employee.dto.WageDTO;
import com.project.pos.employee.dto.responseDTO.WagePageDTO;
import com.project.pos.employee.entity.Wage;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.StoreDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WageService {
    Wage getWageById(Long id);
    WageDTO getWageDTOById(Long id);

    Long createWage(WageDTO dto);
    Long deleteWage(Long id);

    Wage dtoToEntity(WageDTO dto);
    WageDTO entityToDto(Wage entity);

    List<WageDTO> getWageList(StoreDTO storeDTO);
    Page<WagePageDTO> getWagePage(PageRequestDTO requestDTO);
}
