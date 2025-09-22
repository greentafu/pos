package com.project.pos.employee.controller;

import com.project.pos.employee.dto.JobTitleDTO;
import com.project.pos.employee.dto.WageDTO;
import com.project.pos.employee.dto.requestDTO.SaveJobTitleDTO;
import com.project.pos.employee.dto.responseDTO.JobTitlePageDTO;
import com.project.pos.employee.dto.responseDTO.JobTitleScreenAuthDTO;
import com.project.pos.employee.service.EmployeeService;
import com.project.pos.employee.service.JobTitleService;
import com.project.pos.employee.service.WageService;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.ScreenAuthorityDTO;
import com.project.pos.store.dto.ScreenDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.service.ScreenAuthorityService;
import com.project.pos.store.service.ScreenService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class JobTitleApiController {
    @Autowired
    private JobTitleService jobTitleService;
    @Autowired
    private ScreenAuthorityService screenAuthorityService;
    @Autowired
    private WageService wageService;
    @Autowired
    private ScreenService screenService;
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/api/jobTitlePage")
    public Page<JobTitlePageDTO> jobTitlePage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                              @RequestParam(value = "searchText", required = false) String searchText, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).searchText(searchText).storeDTO(storeDTO)
                .build();
        return jobTitleService.getJobTitlePage(requestDTO);
    }
    @GetMapping("/api/getJobTitle")
    public JobTitleScreenAuthDTO getJobTitle(@RequestParam(value = "id") Long id) {
        JobTitleDTO jobTitleDTO=jobTitleService.getJobTitleDTOById(id);
        List<ScreenAuthorityDTO> screenAuthorityDTOList = screenAuthorityService.getScreenAuthByJobTitle(jobTitleDTO);
        return JobTitleScreenAuthDTO.builder()
                .jobTitleDTO(jobTitleDTO)
                .screenAuthorityDTOList(screenAuthorityDTOList)
                .build();
    }
    @PostMapping("/api/saveJobTitle")
    public ResponseEntity<?> saveJobTitle(@Valid @RequestBody SaveJobTitleDTO saveJobTitleDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=saveJobTitleDTO.getId();

        WageDTO wageDTO = wageService.getWageDTOById(saveJobTitleDTO.getWage());

        JobTitleDTO jobTitleDTO = JobTitleDTO.builder()
                .id(tempId==0L? null:tempId)
                .name(saveJobTitleDTO.getName())
                .wageDTO(wageDTO)
                .storeDTO(storeDTO)
                .build();
        JobTitleDTO savedJobtitleDTO = jobTitleService.createJobTitle(jobTitleDTO);

        List<Boolean> authList = saveJobTitleDTO.getScreenAuthList();
        int tempIndex = 0;
        for(Long i=2L; i<14L; i++){
            if(i!=6L){
                ScreenAuthorityDTO authorityDTO = screenAuthorityService.getScreenAuthorityDTOById(savedJobtitleDTO.getId(), i);
                Boolean tempBoolean = authList.get(tempIndex);
                if(authorityDTO!=null){
                    if(authorityDTO.getAuthority()!=tempBoolean) {
                        screenAuthorityService.updateScreenAuthortiy(authorityDTO, authList.get(tempIndex));
                    }
                }else{
                    ScreenDTO screenDTO=screenService.getScreenDTOById(i);
                    ScreenAuthorityDTO screenAuthorityDTO = ScreenAuthorityDTO.builder()
                            .authority(tempBoolean)
                            .jobTitleDTO(savedJobtitleDTO)
                            .screenDTO(screenDTO)
                            .build();
                    screenAuthorityService.createScreenAuthority(screenAuthorityDTO);
                }
                tempIndex++;
            }
        }

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @DeleteMapping("/api/deleteJobTitle")
    public ResponseEntity<?> deleteJobTitle(@RequestParam(value = "id") Long id) {
        JobTitleDTO jobTitleDTO = jobTitleService.getJobTitleDTOById(id);
        Long employeeCount = employeeService.getCountEmployeeByJobTitle(jobTitleDTO);
        if(employeeCount!=0){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("해당 직급을 사용중인 직원이 존재합니다.")));
        }
        screenAuthorityService.deleteScreenAuthorityByJobTitleKey(id);
        jobTitleService.deleteJobTitle(id);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
