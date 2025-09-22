package com.project.pos.employee.service;

import com.project.pos.employee.dto.EmployeeDTO;
import com.project.pos.employee.dto.JobTitleDTO;
import com.project.pos.employee.dto.responseDTO.CommutePageDTO;
import com.project.pos.employee.dto.responseDTO.EmployeePageDTO;
import com.project.pos.employee.entity.Employee;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.home.dto.LoginDTO;
import com.project.pos.store.dto.StoreDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    Employee getEmployeeById(Long id);
    EmployeeDTO getEmployeeDTOById(Long id);

    EmployeeDTO createEmployee(EmployeeDTO dto);
    Long deleteEmployee(Long id);
    EmployeeDTO updateEmployeeData(Long id, String name, String telNumber, JobTitleDTO jobTitleDTO);

    Employee dtoToEntity(EmployeeDTO dto);
    EmployeeDTO entityToDto(Employee entity);

    Long getCountEmployeeByJobTitle(JobTitleDTO jobTitleDTO);
    Long getMaxEmployeeNumber(StoreDTO storeDTO);
    EmployeeDTO getEmployeeByLogin(LoginDTO loginDTO);
    Page<EmployeePageDTO> getEmployeePage(PageRequestDTO requestDTO);
    Page<CommutePageDTO> getCommutePage(PageRequestDTO requestDTO);
}
