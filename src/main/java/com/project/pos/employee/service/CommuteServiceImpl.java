package com.project.pos.employee.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.employee.dto.CommuteDTO;
import com.project.pos.employee.dto.EmployeeDTO;
import com.project.pos.employee.dto.responseDTO.CommutePageDTO;
import com.project.pos.employee.dto.responseDTO.PaymentPageDTO;
import com.project.pos.employee.dto.responseDTO.PaymentSimplePageDTO;
import com.project.pos.employee.entity.Commute;
import com.project.pos.employee.entity.Employee;
import com.project.pos.employee.repository.CommuteRepository;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.entity.Store;
import com.project.pos.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CommuteServiceImpl implements CommuteService {
    private final CommuteRepository commuteRepository;
    private final StoreService storeService;
    private final EmployeeService employeeService;

    @Override
    public Commute getCommuteById(Long id){
        return commuteRepository.findById(id).orElse(null);
    }
    @Override
    public CommuteDTO getCommuteDTOById(Long id){
        Commute entity=getCommuteById(id);
        if(entity==null) return null;
        return entityToDto(entity);
    }

    @Override
    public Long createCommute(CommuteDTO dto){
        Commute entity=dtoToEntity(dto);
        Commute saved=commuteRepository.save(entity);
        return saved.getId();
    }
    @Override
    @Transactional
    public CommuteDTO updateCommuteEndTime(Long id, LocalDateTime end){
        Commute entity=getCommuteById(id);
        if(entity==null) return null;
        entity.setEndTime(end);
        return entityToDto(entity);
    }
    @Override
    @Transactional
    public CommuteDTO updateCommuteModified(Long id){
        Commute entity=getCommuteById(id);
        if(entity==null) return null;
        entity.setModified(true);
        return entityToDto(entity);
    }

    @Override
    public Commute dtoToEntity(CommuteDTO dto){
        return Commute.builder()
                .id(dto.getId())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .jobTitle(dto.getJobTitle())
                .perWage(dto.getPerWage())
                .notes(dto.getNotes())
                .modified(dto.getModified())
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .employee(dto.getEmployeeDTO()!=null? employeeService.dtoToEntity(dto.getEmployeeDTO()):null)
                .build();
    }
    @Override
    public CommuteDTO entityToDto(Commute entity){
        return CommuteDTO.builder()
                .id(entity.getId())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .jobTitle(entity.getJobTitle())
                .perWage(entity.getPerWage())
                .notes(entity.getNotes())
                .modified(entity.getModified())
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .employeeDTO(entity.getEmployee()!=null? employeeService.entityToDto(entity.getEmployee()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public int getCommuteCount(EmployeeDTO employeeDTO, LocalDateTime start, LocalDateTime end){
        Employee employee = employeeService.dtoToEntity(employeeDTO);
        List<Commute> entityList = commuteRepository.getCommuteList(employee, start, end);
        if(entityList.isEmpty()) return 1;

        int result = 0;
        for(Commute entity:entityList){
            String notes = entity.getNotes();
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(notes);

            if(matcher.find()) {
                int temp = Integer.parseInt(matcher.group());
                if(temp>result) result=temp;
            }
        }

        return result+1;
    }
    @Override
    public CommuteDTO getRecentCommuteByEmployee(EmployeeDTO employeeDTO, LocalDateTime start, LocalDateTime end){
        Employee employee = employeeService.dtoToEntity(employeeDTO);
        List<Commute> entityList = commuteRepository.getCommuteListByEmployee(employee, start, end);
        if(entityList.isEmpty()) return null;
        return entityToDto(entityList.get(0));
    }
    @Override
    public List<EmployeeDTO> getWorkingEmployeeList(StoreDTO storeDTO){
        Store store = storeService.dtoToEntity(storeDTO);
        List<Employee> entityList = commuteRepository.getWorkingEmployee(store);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(employeeService::entityToDto).toList();
    }
    @Override
    public Page<CommutePageDTO> getCommuteDetailPage(PageRequestDTO requestDTO){
        return commuteRepository.getCommuteDetailPage(requestDTO);
    }
    @Override
    public Page<PaymentPageDTO> getPaymentPage(PageRequestDTO requestDTO){
        return commuteRepository.getPaymentPage(requestDTO);
    }
    @Override
    public Page<PaymentSimplePageDTO> getPaymentSimplePage(PageRequestDTO requestDTO){
        return commuteRepository.getPaymentSimplePage(requestDTO);
    }
}
