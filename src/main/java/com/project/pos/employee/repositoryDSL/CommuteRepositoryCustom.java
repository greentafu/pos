package com.project.pos.employee.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.employee.dto.responseDTO.CommutePageDTO;
import com.project.pos.employee.dto.responseDTO.PaymentPageDTO;
import com.project.pos.employee.dto.responseDTO.PaymentSimplePageDTO;
import com.project.pos.employee.entity.Employee;
import com.project.pos.store.entity.Store;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommuteRepositoryCustom {
    List<Employee> getWorkingEmployee(Store store);
    Page<CommutePageDTO> getCommuteDetailPage(PageRequestDTO requestDTO);
    Page<PaymentPageDTO> getPaymentPage(PageRequestDTO requestDTO);
    Page<PaymentSimplePageDTO> getPaymentSimplePage(PageRequestDTO requestDTO);
}
