package com.project.pos.employee.controller;

import com.project.pos.config.PasswordUtil;
import com.project.pos.employee.dto.EmployeeDTO;
import com.project.pos.employee.dto.JobTitleDTO;
import com.project.pos.employee.dto.requestDTO.SaveEmployeeDTO;
import com.project.pos.employee.dto.responseDTO.EmployeePageDTO;
import com.project.pos.employee.service.EmployeeService;
import com.project.pos.employee.service.JobTitleService;
import com.project.pos.home.dto.LoginDTO;
import com.project.pos.home.dto.LoginStoreDTO;
import com.project.pos.home.service.LoginService;
import com.project.pos.home.service.LoginStoreService;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.StoreDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class EmployeeApiController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private LoginStoreService loginStoreService;
    @Autowired
    private JobTitleService jobTitleService;

    @GetMapping("/api/employeePage")
    public Page<EmployeePageDTO> employeePage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                              @RequestParam(value = "searchText", required = false) String searchText,
                                              @RequestParam(value = "searchCategory", required = false) Long searchCategory, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).searchText(searchText).searchCategory(searchCategory).storeDTO(storeDTO)
                .build();
        return employeeService.getEmployeePage(requestDTO);
    }
    @GetMapping("/api/getEmployee")
    public EmployeeDTO getEmployee(@RequestParam(value = "id") Long id) {
        return employeeService.getEmployeeDTOById(id);
    }
    @PostMapping("/api/saveEmployee")
    public ResponseEntity<?> saveEmployee(@Valid @RequestBody SaveEmployeeDTO saveEmployeeDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=saveEmployeeDTO.getId();
        String tempPw= saveEmployeeDTO.getUserPw();
        JobTitleDTO jobTitleDTO = jobTitleService.getJobTitleDTOById(saveEmployeeDTO.getJobTitle());

        if(tempId==0L){
            if (saveEmployeeDTO.getUserPw() == null || "".equals(saveEmployeeDTO.getUserPw())) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("errors", List.of("비밀번호를 입력해주세요.")));
            }

            LoginDTO loginDTO = LoginDTO.builder()
                    .userId(saveEmployeeDTO.getUserId())
                    .userPw(PasswordUtil.hashPassword(saveEmployeeDTO.getUserPw()))
                    .typeValue(false)
                    .deleted(false)
                    .build();
            LoginDTO savedLoginDTO = loginService.createLogin(loginDTO);

            LoginStoreDTO loginStoreDTO = LoginStoreDTO.builder()
                    .storeDTO(storeDTO)
                    .loginDTO(savedLoginDTO)
                    .build();
            loginStoreService.createLoginStore(loginStoreDTO);

            Long tempNumber = employeeService.getMaxEmployeeNumber(storeDTO);
            EmployeeDTO employeeDTO = EmployeeDTO.builder()
                    .number(tempNumber==null? 1L:tempNumber+1L)
                    .name(saveEmployeeDTO.getName())
                    .telNumber(saveEmployeeDTO.getTelNumber())
                    .deleted(false)
                    .loginDTO(savedLoginDTO)
                    .job_titleDTO(jobTitleDTO)
                    .build();
            employeeService.createEmployee(employeeDTO);
        }else if(tempPw!=null){
            Long loginId = employeeService.getEmployeeDTOById(tempId).getLoginDTO().getId();
            String pw=PasswordUtil.hashPassword(saveEmployeeDTO.getUserPw());
            loginService.updateLoginPw(loginId, pw);

            employeeService.updateEmployeeData(tempId, saveEmployeeDTO.getName(), saveEmployeeDTO.getTelNumber(), jobTitleDTO);
        }

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @DeleteMapping("/api/deleteEmployee")
    public void deleteEmployee(@RequestParam(value = "id") Long id) {
        EmployeeDTO employeeDTO=employeeService.getEmployeeDTOById(id);
        employeeService.deleteEmployee(id);
        loginService.deleteLogin(employeeDTO.getLoginDTO().getId());
    }
    @GetMapping("/api/makeEmployeeId")
    public String makeEmployeeId(HttpSession session){
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        String ownerCode = storeDTO.getOwnerDTO().getOwnerCode();
        Long maxNumber = employeeService.getMaxEmployeeNumber(storeDTO);
        String format = String.format("%04d", maxNumber!=null? maxNumber+1L:1L);
        String year = String.valueOf(LocalDate.now().getYear()).substring(2);
        return year+"-"+ownerCode+"-"+format;
    }
}
