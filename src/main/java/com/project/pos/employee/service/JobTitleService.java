package com.project.pos.employee.service;

import com.project.pos.employee.dto.JobTitleDTO;
import com.project.pos.employee.dto.WageDTO;
import com.project.pos.employee.dto.responseDTO.JobTitlePageDTO;
import com.project.pos.employee.entity.JobTitle;
import com.project.pos.dslDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface JobTitleService {
    JobTitle getJobTitleById(Long id);
    JobTitleDTO getJobTitleDTOById(Long id);

    JobTitleDTO createJobTitle(JobTitleDTO dto);
    Long deleteJobTitle(Long id);

    JobTitle dtoToEntity(JobTitleDTO dto);
    JobTitleDTO entityToDto(JobTitle entity);

    Long getCountJobTitleByWage(WageDTO wageDTO);
    List<JobTitleDTO> getJobTitleList();
    Page<JobTitlePageDTO> getJobTitlePage(PageRequestDTO requestDTO);
}
