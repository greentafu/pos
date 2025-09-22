package com.project.pos.store.service;

import com.project.pos.store.dto.ScreenDTO;
import com.project.pos.store.entity.Screen;
import com.project.pos.store.repository.ScreenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScreenServiceImpl implements ScreenService{
    private final ScreenRepository screenRepository;

    @Override
    public Screen getScreenById(Long id){
        return screenRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 화면 없음"));
    }
    @Override
    public ScreenDTO getScreenDTOById(Long id){
        Screen entity=getScreenById(id);
        return entityToDto(entity);
    }

    @Override
    public Long createScreen(ScreenDTO dto){
        Screen entity=dtoToEntity(dto);
        Screen saved=screenRepository.save(entity);
        return saved.getId();
    }
    @Override
    public Long deleteScreen(Long id){
        screenRepository.deleteById(id);
        return id;
    }

    @Override
    public Screen dtoToEntity(ScreenDTO dto){
        return Screen.builder()
                .id(dto.getId())
                .name(dto.getName())
                .imgUrl(dto.getImgUrl())
                .url(dto.getUrl())
                .build();
    }
    @Override
    public ScreenDTO entityToDto(Screen entity){
        return ScreenDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .imgUrl(entity.getImgUrl())
                .url(entity.getUrl())
                .build();
    }
}
