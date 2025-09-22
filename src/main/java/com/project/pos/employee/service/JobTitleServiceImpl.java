package com.project.pos.employee.service;

import com.project.pos.employee.dto.JobTitleDTO;
import com.project.pos.employee.dto.WageDTO;
import com.project.pos.employee.dto.responseDTO.JobTitlePageDTO;
import com.project.pos.employee.entity.JobTitle;
import com.project.pos.employee.entity.Wage;
import com.project.pos.employee.repository.JobTitleRepository;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.service.StoreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobTitleServiceImpl implements JobTitleService {
    private final JobTitleRepository jobTitleRepository;
    private final StoreService storeService;
    private final WageService wageService;

    @Override
    public JobTitle getJobTitleById(Long id){
        return jobTitleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 직급 없음"));
    }
    @Override
    public JobTitleDTO getJobTitleDTOById(Long id){
        JobTitle entity=getJobTitleById(id);
        return entityToDto(entity);
    }

    @Override
    public JobTitleDTO createJobTitle(JobTitleDTO dto){
        JobTitle entity=dtoToEntity(dto);
        JobTitle saved=jobTitleRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public Long deleteJobTitle(Long id){
        JobTitle entity = getJobTitleById(id);
        entity.setDeleted(true);
        return id;
    }

    @Override
    public JobTitle dtoToEntity(JobTitleDTO dto){
        return JobTitle.builder()
                .id(dto.getId())
                .name(dto.getName())
                .wage(dto.getWageDTO()!=null? wageService.dtoToEntity(dto.getWageDTO()):null)
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public JobTitleDTO entityToDto(JobTitle entity){
        return JobTitleDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .wageDTO(entity.getWage()!=null? wageService.entityToDto(entity.getWage()):null)
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public Long getCountJobTitleByWage(WageDTO wageDTO){
        Wage wage=wageService.dtoToEntity(wageDTO);
        return jobTitleRepository.countJobTitleByWage(wage);
    }
    @Override
    public List<JobTitleDTO> getJobTitleList(){
        List<JobTitle> entityList = jobTitleRepository.findAll(Sort.by("id"));
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public Page<JobTitlePageDTO> getJobTitlePage(PageRequestDTO requestDTO){
        return jobTitleRepository.getJobTitlePage(requestDTO);
    }
}
