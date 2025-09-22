package com.project.pos.employee.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.employee.dto.CommuteDTO;
import com.project.pos.employee.dto.EmployeeDTO;
import com.project.pos.employee.dto.responseDTO.CommutePageDTO;
import com.project.pos.employee.dto.responseDTO.PaymentPageDTO;
import com.project.pos.employee.dto.responseDTO.PaymentSimplePageDTO;
import com.project.pos.employee.entity.Commute;
import com.project.pos.store.dto.StoreDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface CommuteService {
    Commute getCommuteById(Long id);
    CommuteDTO getCommuteDTOById(Long id);

    Long createCommute(CommuteDTO dto);
    CommuteDTO updateCommuteEndTime(Long id, LocalDateTime end);
    CommuteDTO updateCommuteModified(Long id);

    Commute dtoToEntity(CommuteDTO dto);
    CommuteDTO entityToDto(Commute entity);

    int getCommuteCount(EmployeeDTO employeeDTO, LocalDateTime start, LocalDateTime end);
    CommuteDTO getRecentCommuteByEmployee(EmployeeDTO employeeDTO, LocalDateTime start, LocalDateTime end);
    List<EmployeeDTO> getWorkingEmployeeList(StoreDTO storeDTO);
    Page<CommutePageDTO> getCommuteDetailPage(PageRequestDTO requestDTO);
    Page<PaymentPageDTO> getPaymentPage(PageRequestDTO requestDTO);
    Page<PaymentSimplePageDTO> getPaymentSimplePage(PageRequestDTO requestDTO);
}
