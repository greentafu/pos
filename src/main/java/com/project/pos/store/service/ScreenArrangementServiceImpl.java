package com.project.pos.store.service;

import com.project.pos.store.dto.ScreenArrangementDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.entity.ScreenArrangement;
import com.project.pos.store.entity.ScreenArrangementID;
import com.project.pos.store.entity.Store;
import com.project.pos.store.repository.ScreenArrangementRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreenArrangementServiceImpl implements ScreenArrangementService{
    private final ScreenArrangementRepository screenArrangementRepository;
    private final ScreenService screenService;
    private final StoreService storeService;

    @Override
    public ScreenArrangement getScreenArrangementById(Long storeId, Long screenId){
        return screenArrangementRepository.findById(new ScreenArrangementID(storeId, screenId))
                .orElseThrow(() -> new EntityNotFoundException("해당 화면순서 없음"));
    }
    @Override
    public ScreenArrangementDTO getScreenArrangementDTOById(Long storeId, Long screenId){
        ScreenArrangement entity=getScreenArrangementById(storeId, screenId);
        return entityToDto(entity);
    }

    @Override
    public ScreenArrangementID createScreenArrangement(ScreenArrangementDTO dto){
        ScreenArrangement entity=dtoToEntity(dto);
        ScreenArrangement saved=screenArrangementRepository.save(entity);
        return saved.getId();
    }
    @Override
    public ScreenArrangementID deleteScreenArrangement(Long storeId, Long screenId){
        ScreenArrangementID id=new ScreenArrangementID(storeId, screenId);
        screenArrangementRepository.deleteById(id);
        return id;
    }
    @Override
    @Transactional
    public void updateScreenArrangement(Long storeId, Long screenId, Integer page, Integer index){
        ScreenArrangement entity = getScreenArrangementById(storeId, screenId);
        entity.setPage(page);
        entity.setIndexValue(index);
    }

    @Override
    public ScreenArrangement dtoToEntity(ScreenArrangementDTO dto){
        return ScreenArrangement.builder()
                .id(new ScreenArrangementID(dto.getStoreId(), dto.getScreenId()))
                .page(dto.getPage())
                .indexValue(dto.getIndexValue())
                .screen(dto.getScreenDTO()!=null? screenService.dtoToEntity(dto.getScreenDTO()):null)
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public ScreenArrangementDTO entityToDto(ScreenArrangement entity){
        return ScreenArrangementDTO.builder()
                .storeId(entity.getId().getStoreKey())
                .screenId(entity.getId().getScreenKey())
                .page(entity.getPage())
                .indexValue(entity.getIndexValue())
                .screenDTO(entity.getScreen()!=null? screenService.entityToDto(entity.getScreen()):null)
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .build();
    }

    @Override
    public List<ScreenArrangementDTO> pageList(StoreDTO storeDTO, int page){
        Store store=storeService.dtoToEntity(storeDTO);
        List<ScreenArrangement> entityList = page==1? screenArrangementRepository.page1List(store): screenArrangementRepository.page2List(store);
        return entityList.stream().map(this::entityToDto).toList();
    }
}
