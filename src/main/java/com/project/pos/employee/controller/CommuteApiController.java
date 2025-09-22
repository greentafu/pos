package com.project.pos.employee.controller;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.employee.dto.CommuteDTO;
import com.project.pos.employee.dto.EmployeeDTO;
import com.project.pos.employee.dto.requestDTO.SaveCommuteDTO;
import com.project.pos.employee.dto.requestDTO.UpdateCommuteDTO;
import com.project.pos.employee.dto.responseDTO.CommutePageDTO;
import com.project.pos.employee.service.CommuteService;
import com.project.pos.employee.service.EmployeeService;
import com.project.pos.home.dto.LoginDTO;
import com.project.pos.home.service.LoginService;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@Log4j2
public class CommuteApiController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private CommuteService commuteService;

    @GetMapping("/api/commutePage")
    public Page<CommutePageDTO> commutePage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                            @RequestParam(value = "searchText", required = false) String searchText, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.toLocalDate().atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).searchText(searchText).startDate(start).endDate(end).storeDTO(storeDTO)
                .build();
        return employeeService.getCommutePage(requestDTO);
    }
    @GetMapping("/api/commuteDetailPage")
    public Page<CommutePageDTO> commuteDetailPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                                  @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,
                                                  @RequestParam(value = "id") Long id) {
        LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
        LocalDate tempEnd = LocalDate.parse(endDate);
        LocalDateTime end = tempEnd.atTime(LocalTime.MAX);

        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).startDate(start).endDate(end).selectedId(id)
                .build();
        return commuteService.getCommuteDetailPage(requestDTO);
    }
    @GetMapping("/api/workingEmployee")
    public List<EmployeeDTO> workingEmployee(HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        return commuteService.getWorkingEmployeeList(storeDTO);
    }
    @GetMapping("/api/getCommuteEmployee")
    public CommutePageDTO getCommuteEmployee(@RequestParam(value = "id") Long id) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.toLocalDate().atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        EmployeeDTO employeeDTO=employeeService.getEmployeeDTOById(id);
        CommuteDTO commuteDTO=commuteService.getRecentCommuteByEmployee(employeeDTO, start, end);

        LocalDateTime startTime=(commuteDTO!=null)?commuteDTO.getStartTime():null;
        LocalDateTime endTime=(commuteDTO!=null)?commuteDTO.getEndTime():null;

        return new CommutePageDTO(null, null, employeeDTO.getName(), null,
                (startTime!=null)?startTime.toString():null, (endTime!=null)?endTime.toString():null, null);
    }
    @GetMapping("/api/getCommuteTime")
    public ResponseEntity<?> getCommuteTime(@RequestParam(value = "id") Long id) {
        CommuteDTO commuteDTO=commuteService.getCommuteDTOById(id);
        if (commuteDTO.getModified()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("수정된 기록은 다시 수정할 수 없습니다.")));
        }
        return ResponseEntity.ok(commuteDTO);
    }
    @GetMapping("/api/checkAdmin")
    public ResponseEntity<?> checkAdmin(@RequestParam(value = "id") Long id, @RequestParam(value = "pw") String pw,
                                        HttpSession session) {
        if (id==null || id.equals(0L)) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("직원을 선택해 주세요.")));
        }
        if (pw==null || pw.equals("")) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("관리자번호를 입력해 주세요.")));
        }

        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        LoginDTO dto=loginService.loginProcess(storeDTO.getOwnerDTO().getLoginDTO().getUserId(), pw);
        if (dto==null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("관리자번호가 일치하지 않습니다.")));
        }
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @PostMapping("/api/saveCommuteStart")
    public ResponseEntity<?> saveCommuteStart(@Valid @RequestBody SaveCommuteDTO saveCommuteDTO, HttpSession session) {
        if (saveCommuteDTO.getId()==0L) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("직원을 선택해 주세요.")));
        }
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        EmployeeDTO employeeDTO = employeeService.getEmployeeDTOById(saveCommuteDTO.getId());
        LoginDTO dto=loginService.loginProcess(employeeDTO.getLoginDTO().getUserId(), saveCommuteDTO.getUserPw());

        if(dto!=null){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime start = now.toLocalDate().atStartOfDay();
            LocalDateTime end = start.plusDays(1);
            int count = commuteService.getCommuteCount(employeeDTO, start, end);

            String notes = count+"차 출근";
            CommuteDTO commuteDTO = CommuteDTO.builder()
                    .startTime(now)
                    .jobTitle(employeeDTO.getJob_titleDTO().getName())
                    .perWage(employeeDTO.getJob_titleDTO().getWageDTO().getPerWage())
                    .notes(notes)
                    .modified(false)
                    .storeDTO(storeDTO)
                    .employeeDTO(employeeDTO)
                    .build();
            commuteService.createCommute(commuteDTO);
        }

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @PutMapping("/api/saveCommuteEnd")
    public ResponseEntity<?> saveCommuteEnd(@Valid @RequestBody SaveCommuteDTO saveCommuteDTO, HttpSession session) {
        if (saveCommuteDTO.getId()==0L) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("직원을 선택해 주세요.")));
        }
        EmployeeDTO employeeDTO = employeeService.getEmployeeDTOById(saveCommuteDTO.getId());
        LoginDTO dto=loginService.loginProcess(employeeDTO.getLoginDTO().getUserId(), saveCommuteDTO.getUserPw());

        if(dto!=null){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime start = now.toLocalDate().atStartOfDay();
            LocalDateTime end = start.plusDays(1);
            CommuteDTO commuteDTO = commuteService.getRecentCommuteByEmployee(employeeDTO, start, end);
            if(commuteDTO.getEndTime()==null) commuteService.updateCommuteEndTime(commuteDTO.getId(), now);
        }

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @PutMapping("/api/updateCommuteTime")
    public ResponseEntity<?> updateCommuteTime(@Valid @RequestBody UpdateCommuteDTO updateCommuteDTO) {
        if (updateCommuteDTO.getId()==0L) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("변경할 기록을 선택해 주세요.")));
        }
        CommuteDTO commuteDTO=commuteService.getCommuteDTOById(updateCommuteDTO.getId());
        LocalDateTime start = commuteDTO.getStartTime()
                .withHour(updateCommuteDTO.getStartHour())
                .withMinute(updateCommuteDTO.getStartMinute());
        if(commuteDTO.getEndTime()==null){
            LocalDateTime end = LocalDateTime.now()
                    .withHour(updateCommuteDTO.getEndHour())
                    .withMinute(updateCommuteDTO.getEndMinute());
            if (start.isAfter(end)) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("errors", List.of("출근시간은 퇴근시간보다 늦을 수 없습니다.")));
            }

            CommuteDTO savedCommuteDTO = commuteService.updateCommuteEndTime(commuteDTO.getId(), end);
            if(!savedCommuteDTO.getStartTime().equals(start)){
                String notes = savedCommuteDTO.getNotes();
                if(notes==null) notes = "1차 출근 1차 수정";
                else {
                    if(notes.endsWith("출근")) notes+=" 1차 수정";
                    else{
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(notes);

                        List<String> numbers = new ArrayList<>();
                        while (matcher.find()) numbers.add(matcher.group());

                        notes = numbers.get(0)+"차 출근 "+(Long.parseLong(numbers.get(1))+1L)+"차 수정";
                    }
                }
                CommuteDTO newCommuteDTO = CommuteDTO.builder()
                        .startTime(start)
                        .endTime(end)
                        .jobTitle(savedCommuteDTO.getJobTitle())
                        .perWage(savedCommuteDTO.getPerWage())
                        .notes(notes)
                        .modified(false)
                        .storeDTO(savedCommuteDTO.getStoreDTO())
                        .employeeDTO(savedCommuteDTO.getEmployeeDTO())
                        .build();
                commuteService.createCommute(newCommuteDTO);
                commuteService.updateCommuteModified(savedCommuteDTO.getId());
            }
        }else{
            LocalDateTime end = commuteDTO.getEndTime()
                    .withHour(updateCommuteDTO.getEndHour())
                    .withMinute(updateCommuteDTO.getEndMinute());
            if (start.isAfter(end)) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("errors", List.of("출근시간은 퇴근시간보다 늦을 수 없습니다.")));
            }

            String notes = commuteDTO.getNotes();
            if(notes.endsWith("출근")) notes+=" 1차 수정";
            else{
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(notes);

                List<String> numbers = new ArrayList<>();
                while (matcher.find()) numbers.add(matcher.group());

                notes = numbers.get(0)+"차 출근 "+(Long.parseLong(numbers.get(1))+1L)+"차 수정";
            }
            CommuteDTO newCommuteDTO = CommuteDTO.builder()
                    .startTime(start)
                    .endTime(end)
                    .jobTitle(commuteDTO.getJobTitle())
                    .perWage(commuteDTO.getPerWage())
                    .notes(notes)
                    .modified(false)
                    .storeDTO(commuteDTO.getStoreDTO())
                    .employeeDTO(commuteDTO.getEmployeeDTO())
                    .build();
            commuteService.createCommute(newCommuteDTO);
            commuteService.updateCommuteModified(commuteDTO.getId());
        }

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
