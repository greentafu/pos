package com.project.pos.employee.service;

import com.project.pos.employee.dto.WageDTO;
import com.project.pos.employee.dto.responseDTO.WagePageDTO;
import com.project.pos.employee.entity.Wage;
import com.project.pos.employee.repository.WageRepository;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.entity.Store;
import com.project.pos.store.service.StoreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WageServiceImpl implements WageService {
    private final WageRepository wageRepository;
    private final StoreService storeService;

    @Override
    public Wage getWageById(Long id){
        return wageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 시급 없음"));
    }
    @Override
    public WageDTO getWageDTOById(Long id){
        Wage entity=getWageById(id);
        return entityToDto(entity);
    }

    @Override
    public Long createWage(WageDTO dto){
        Wage entity=dtoToEntity(dto);
        Wage saved=wageRepository.save(entity);
        return saved.getId();
    }
    @Override
    @Transactional
    public Long deleteWage(Long id){
        Wage wage=getWageById(id);
        wage.setDeleted(true);
        return id;
    }

    @Override
    public Wage dtoToEntity(WageDTO dto){
        return Wage.builder()
                .id(dto.getId())
                .name(dto.getName())
                .perWage(dto.getPerWage())
                .notes(dto.getNotes())
                .deleted(dto.getDeleted())
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public WageDTO entityToDto(Wage entity){
        return WageDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .perWage(entity.getPerWage())
                .notes(entity.getNotes())
                .deleted(entity.getDeleted())
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public List<WageDTO> getWageList(StoreDTO storeDTO){
        Store store=storeService.dtoToEntity(storeDTO);
        List<Wage> entityList = wageRepository.getWageListByStore(store);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public Page<WagePageDTO> getWagePage(PageRequestDTO requestDTO){
        return wageRepository.getWagePage(requestDTO);
    }
}
