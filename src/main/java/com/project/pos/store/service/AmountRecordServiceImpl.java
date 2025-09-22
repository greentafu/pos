package com.project.pos.store.service;

import com.project.pos.employee.dto.EmployeeDTO;
import com.project.pos.employee.entity.Employee;
import com.project.pos.employee.service.EmployeeService;
import com.project.pos.store.dto.AmountRecordDTO;
import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.entity.AmountRecord;
import com.project.pos.store.entity.Pos;
import com.project.pos.store.entity.Store;
import com.project.pos.store.repository.AmountRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AmountRecordServiceImpl implements AmountRecordService {
    private final AmountRecordRepository amountRecordRepository;
    private final StoreService storeService;
    private final PosService posService;
    private final EmployeeService employeeService;

    @Override
    public AmountRecord getAmountRecordById(Long id){
        return amountRecordRepository.findById(id).orElse(null);
    }
    @Override
    public AmountRecordDTO getAmountRecordDTOById(Long id){
        AmountRecord entity=getAmountRecordById(id);
        if(entity==null) return null;
        return entityToDto(entity);
    }

    @Override
    public AmountRecordDTO createAmountRecord(AmountRecordDTO dto){
        AmountRecord entity=dtoToEntity(dto);
        AmountRecord saved=amountRecordRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public AmountRecordDTO updateAmountRecordReopen(Long id, Long amount, EmployeeDTO employeeDTO){
        AmountRecord entity=getAmountRecordById(id);
        if(amount!=null) entity.setAmount(amount);
        Employee employee=employeeService.getEmployeeById(employeeDTO.getId());
        entity.setOpenEmployee(employee);
        return entityToDto(entity);
    }
    @Override
    @Transactional
    public AmountRecordDTO updateAmountRecord(Long id, Long estimate, Long finished, EmployeeDTO employeeDTO){
        AmountRecord entity=getAmountRecordById(id);
        Employee employee=employeeService.getEmployeeById(employeeDTO.getId());
        entity.setEstimate(estimate);
        entity.setFinished(finished);
        entity.setCloseEmployee(employee);
        return entityToDto(entity);
    }

    @Override
    public AmountRecord dtoToEntity(AmountRecordDTO dto){
        return AmountRecord.builder()
                .id(dto.getId())
                .amount(dto.getAmount())
                .estimate(dto.getEstimate())
                .finished(dto.getFinished())
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .pos(dto.getPosDTO()!=null? posService.dtoToEntity(dto.getPosDTO()):null)
                .openEmployee(dto.getOpenEmployeeDTO()!=null? employeeService.dtoToEntity(dto.getOpenEmployeeDTO()):null)
                .closeEmployee(dto.getCloseEmployeeDTO()!=null? employeeService.dtoToEntity(dto.getCloseEmployeeDTO()):null)
                .build();
    }
    @Override
    public AmountRecordDTO entityToDto(AmountRecord entity){
        return AmountRecordDTO.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .estimate(entity.getEstimate())
                .finished(entity.getFinished())
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .posDTO(entity.getPos()!=null? posService.entityToDto(entity.getPos()):null)
                .openEmployeeDTO(entity.getOpenEmployee()!=null? employeeService.entityToDto(entity.getOpenEmployee()):null)
                .closeEmployeeDTO(entity.getCloseEmployee()!=null? employeeService.entityToDto(entity.getCloseEmployee()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public AmountRecordDTO getExistAmountRecord(StoreDTO storeDTO, PosDTO posDTO){
        Store store=storeService.dtoToEntity(storeDTO);
        Pos pos=posService.dtoToEntity(posDTO);
        AmountRecord entity = amountRecordRepository.getExistAmountRecord(store, pos);
        if(entity==null) return null;
        return entityToDto(entity);
    }
}
