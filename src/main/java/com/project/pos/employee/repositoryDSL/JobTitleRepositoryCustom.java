package com.project.pos.employee.repositoryDSL;

import com.project.pos.employee.dto.responseDTO.JobTitlePageDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface JobTitleRepositoryCustom {
    Page<JobTitlePageDTO> getJobTitlePage(PageRequestDTO requestDTO);
}
