package com.project.pos.store.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.dto.responseDTO.PosPageDTO;
import com.project.pos.store.entity.Pos;
import com.project.pos.store.entity.Store;
import com.project.pos.store.repository.PosRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PosServiceImpl implements PosService {
    private final PosRepository posRepository;
    private final StoreService storeService;

    @Override
    public Pos getPosById(Long id){
        return posRepository.findById(id).orElse(null);
    }
    @Override
    public PosDTO getPosDTOById(Long id){
        Pos entity=getPosById(id);
        if(entity==null) return null;
        return entityToDto(entity);
    }

    @Override
    public Long createPos(PosDTO dto){
        Pos entity=dtoToEntity(dto);
        Pos saved=posRepository.save(entity);
        return saved.getId();
    }
    @Override
    @Transactional
    public Long deletePos(Long id){
        Pos entity = getPosById(id);
        entity.setDeleted(true);
        return id;
    }

    @Override
    public Pos dtoToEntity(PosDTO dto){
        return Pos.builder()
                .id(dto.getId())
                .number(dto.getNumber())
                .name(dto.getName())
                .machineId(dto.getMachineId())
                .location(dto.getLocation())
                .status(dto.getStatus())
                .deleted(dto.getDeleted())
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public PosDTO entityToDto(Pos entity){
        return PosDTO.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .name(entity.getName())
                .machineId(entity.getMachineId())
                .location(entity.getLocation())
                .status(entity.getStatus())
                .deleted(entity.getDeleted())
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .build();
    }

    @Override
    public Long countPosByNumber(StoreDTO storeDTO, Long number){
        Store store=storeService.dtoToEntity(storeDTO);
        return posRepository.countPosByNumber(store, number);
    }
    @Override
    public Long countPosByMachineId(StoreDTO storeDTO, String machineId){
        Store store=storeService.dtoToEntity(storeDTO);
        return posRepository.countPosByMachineID(store, machineId);
    }
    @Override
    public List<PosDTO> getPosByStore(StoreDTO storeDTO){
        Store store = storeService.dtoToEntity(storeDTO);
        List<Pos> entityList = posRepository.getPosListByStore(store);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public Page<PosPageDTO> getPosPage(PageRequestDTO requestDTO){
        return posRepository.getPosPage(requestDTO);
    }
}
