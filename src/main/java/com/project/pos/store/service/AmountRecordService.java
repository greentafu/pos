package com.project.pos.store.service;

import com.project.pos.employee.dto.EmployeeDTO;
import com.project.pos.store.dto.AmountRecordDTO;
import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.entity.AmountRecord;

public interface AmountRecordService {
    AmountRecord getAmountRecordById(Long id);
    AmountRecordDTO getAmountRecordDTOById(Long id);

    AmountRecordDTO createAmountRecord(AmountRecordDTO dto);
    AmountRecordDTO updateAmountRecordReopen(Long id, Long amount, EmployeeDTO employeeDTO);
    AmountRecordDTO updateAmountRecord(Long id, Long estimate, Long finished, EmployeeDTO employeeDTO);

    AmountRecord dtoToEntity(AmountRecordDTO dto);
    AmountRecordDTO entityToDto(AmountRecord entity);

    AmountRecordDTO getExistAmountRecord(StoreDTO storeDTO, PosDTO posDTO);
}
