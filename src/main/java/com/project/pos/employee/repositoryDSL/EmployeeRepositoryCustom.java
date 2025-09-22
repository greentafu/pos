package com.project.pos.employee.repositoryDSL;

import com.project.pos.employee.dto.responseDTO.CommutePageDTO;
import com.project.pos.employee.dto.responseDTO.EmployeePageDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import org.springframework.data.domain.Page;

public interface EmployeeRepositoryCustom {
    Page<EmployeePageDTO> getEmployeePage(PageRequestDTO requestDTO);
    Page<CommutePageDTO> getCommutePage(PageRequestDTO requestDTO);
}
