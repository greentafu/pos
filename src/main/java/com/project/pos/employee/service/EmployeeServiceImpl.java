package com.project.pos.employee.service;

import com.project.pos.employee.dto.EmployeeDTO;
import com.project.pos.employee.dto.JobTitleDTO;
import com.project.pos.employee.dto.responseDTO.CommutePageDTO;
import com.project.pos.employee.dto.responseDTO.EmployeePageDTO;
import com.project.pos.employee.entity.Employee;
import com.project.pos.employee.entity.JobTitle;
import com.project.pos.employee.repository.EmployeeRepository;
import com.project.pos.home.dto.LoginDTO;
import com.project.pos.home.entity.Login;
import com.project.pos.home.service.LoginService;
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
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final LoginService loginService;
    private final JobTitleService jobTitleService;
    private final StoreService storeService;

    @Override
    public Employee getEmployeeById(Long id){
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 직원 없음"));
    }
    @Override
    public EmployeeDTO getEmployeeDTOById(Long id){
        Employee entity=getEmployeeById(id);
        return entityToDto(entity);
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO dto){
        Employee entity=dtoToEntity(dto);
        Employee saved=employeeRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public Long deleteEmployee(Long id){
        Employee entity = getEmployeeById(id);
        entity.setDeleted(true);
        return id;
    }
    @Override
    @Transactional
    public EmployeeDTO updateEmployeeData(Long id, String name, String telNumber, JobTitleDTO jobTitleDTO){
        Employee entity = getEmployeeById(id);
        JobTitle jobTitle = jobTitleService.dtoToEntity(jobTitleDTO);
        entity.setName(name);
        entity.setTelNumber(telNumber);
        entity.setJobTitle(jobTitle);
        return entityToDto(entity);
    }

    @Override
    public Employee dtoToEntity(EmployeeDTO dto){
        return Employee.builder()
                .id(dto.getId())
                .number(dto.getNumber())
                .name(dto.getName())
                .telNumber(dto.getTelNumber())
                .deleted(dto.getDeleted())
                .login(dto.getLoginDTO()!=null? loginService.dtoToEntity(dto.getLoginDTO()):null)
                .jobTitle(dto.getJob_titleDTO()!=null? jobTitleService.dtoToEntity(dto.getJob_titleDTO()):null)
                .build();
    }
    @Override
    public EmployeeDTO entityToDto(Employee entity){
        return EmployeeDTO.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .name(entity.getName())
                .telNumber(entity.getTelNumber())
                .deleted(entity.getDeleted())
                .loginDTO(entity.getLogin()!=null? loginService.entityToDto(entity.getLogin()):null)
                .job_titleDTO(entity.getJobTitle()!=null? jobTitleService.entityToDto(entity.getJobTitle()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public Long getCountEmployeeByJobTitle(JobTitleDTO jobTitleDTO){
        JobTitle jobTitle=jobTitleService.dtoToEntity(jobTitleDTO);
        return employeeRepository.countEmployeeByJobTitle(jobTitle);
    }
    @Override
    public Long getMaxEmployeeNumber(StoreDTO storeDTO){
        Store store=storeService.dtoToEntity(storeDTO);
        return employeeRepository.maxEmployeeNumber(store);
    }
    @Override
    public EmployeeDTO getEmployeeByLogin(LoginDTO loginDTO){
        Login login = loginService.dtoToEntity(loginDTO);
        Employee entity = employeeRepository.getEmployeeByLogin(login);
        if(entity==null) return null;
        return entityToDto(entity);
    }
    @Override
    public Page<EmployeePageDTO> getEmployeePage(PageRequestDTO requestDTO){
        return employeeRepository.getEmployeePage(requestDTO);
    }
    @Override
    public Page<CommutePageDTO> getCommutePage(PageRequestDTO requestDTO){
        return employeeRepository.getCommutePage(requestDTO);
    }
}
