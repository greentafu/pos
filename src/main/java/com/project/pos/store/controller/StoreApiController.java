package com.project.pos.store.controller;

import com.project.pos.employee.dto.EmployeeDTO;
import com.project.pos.employee.service.EmployeeService;
import com.project.pos.home.dto.LoginDTO;
import com.project.pos.home.dto.LoginStoreDTO;
import com.project.pos.home.dto.OwnerDTO;
import com.project.pos.home.service.LoginService;
import com.project.pos.home.service.LoginStoreService;
import com.project.pos.home.service.OwnerService;
import com.project.pos.revenue.service.PaymentMethodService;
import com.project.pos.store.dto.*;
import com.project.pos.store.dto.requestDTO.*;
import com.project.pos.store.dto.responseDTO.StorePageDTO;
import com.project.pos.store.service.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class StoreApiController {
    @Autowired
    private LoginStoreService loginStoreService;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreTimeService storeTimeService;
    @Autowired
    private ScreenService screenService;
    @Autowired
    private ScreenArrangementService screenArrangementService;
    @Autowired
    private DayOfWeekService dayOfWeekService;
    @Autowired
    private PosService posService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AmountRecordService amountRecordService;
    @Autowired
    private PaymentMethodService paymentMethodService;

    @PostMapping("/api/saveStore")
    public ResponseEntity<?> saveStore(@Valid @RequestBody SaveStoreDTO saveStoreDTO, HttpSession session) {
        LoginDTO loginDTO = (LoginDTO) session.getAttribute("loginUser");
        OwnerDTO ownerDTO = ownerService.getOwnerDTOByLogin(loginDTO);
        String tempPercent = saveStoreDTO.getPointPercent();
        Long pointPercent = 0L;
        if(tempPercent!=null && tempPercent!="") pointPercent = Long.parseLong(tempPercent.replace(",", ""));
        StoreDTO storeDTO = StoreDTO.builder()
                .name(saveStoreDTO.getName())
                .location(saveStoreDTO.getLocation())
                .telNumber(saveStoreDTO.getTelNumber())
                .pointPercent(pointPercent)
                .status(saveStoreDTO.getStatus())
                .deleted(false)
                .ownerDTO(ownerDTO)
                .build();
        StoreDTO savedStoreDTO =storeService.createStore(storeDTO);

        List<TimeDTO> timeDTOList = saveStoreDTO.getTimeList();
        List<DayOfWeekDTO> dayList = dayOfWeekService.getDayOfWeekList();
        for(int i=0; i<7; i++){
            StoreTimeDTO storeTimeDTO = StoreTimeDTO.builder()
                    .startTime(timeDTOList.get(i).getStartTime())
                    .endTime(timeDTOList.get(i).getEndTime())
                    .statusValue(timeDTOList.get(i).getStatusValue())
                    .dayOfWeekDTO(dayList.get(i))
                    .storeDTO(savedStoreDTO)
                    .build();
            storeTimeService.createStoreTime(storeTimeDTO);
        }

        LoginStoreDTO loginStoreDTO=LoginStoreDTO.builder()
                .loginDTO(loginDTO)
                .storeDTO(savedStoreDTO)
                .build();
        loginStoreService.createLoginStore(loginStoreDTO);

        for(Long i=1L; i<14L; i++){
            ScreenDTO screenDTO=screenService.getScreenDTOById(i);
            Integer page=i<7L? 1:2;
            Integer indexValue=i<7L? i.intValue()-1: i.intValue()-7;
            ScreenArrangementDTO screenArrangementDTO=ScreenArrangementDTO.builder()
                    .page(page)
                    .indexValue(indexValue)
                    .screenDTO(screenDTO)
                    .storeDTO(savedStoreDTO)
                    .build();
            screenArrangementService.createScreenArrangement(screenArrangementDTO);
        }
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @PutMapping("/api/updateStore")
    public ResponseEntity<?> updateStore(@Valid @RequestBody ModifyStoreDTO modifyStoreDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        String tempPercent = modifyStoreDTO.getPointPercent();
        Long pointPercent = 0L;
        if(tempPercent!=null && tempPercent!="") pointPercent = Long.parseLong(tempPercent.replace(",", ""));
        storeService.updateStore(storeDTO.getId(), modifyStoreDTO.getName(), modifyStoreDTO.getLocation(),
                modifyStoreDTO.getTelNumber(), pointPercent, modifyStoreDTO.getStatus());
        StoreDTO savedStoreDTO = storeService.getStoreDTOById(storeDTO.getId());
        session.setAttribute("currentStore", savedStoreDTO);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @PutMapping("/api/modifyStoreTime")
    public ResponseEntity<?> modifyStoreTime(@Valid @RequestBody List<TimeDTO> timeList, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<DayOfWeekDTO> dayList = dayOfWeekService.getDayOfWeekList();
        for(int i=0; i<7; i++){
            DayOfWeekDTO tempDay = dayList.get(i);
            TimeDTO tempTime = timeList.get(i);
            storeTimeService.updateStoreTime(storeDTO.getId(), tempDay.getId(),
                    tempTime.getStartTime(), tempTime.getEndTime(), tempTime.getStatusValue());
        }
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @GetMapping("/api/getStorePage")
    public StorePageDTO getStoreTime(HttpSession session){
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<StoreTimeDTO> storeTimeDTOList = storeTimeService.getStoreTimeListByStoreDTO(storeDTO);
        return new StorePageDTO(storeDTO, storeTimeDTOList);
    }
    @DeleteMapping("/api/deleteStore")
    public void deleteStore(HttpSession session){
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        storeService.deleteStore(storeDTO.getId());
    }
    @PostMapping("/api/openStore")
    public ResponseEntity<?> openStore(@Valid @RequestBody SaveOpenStoreDTO saveOpenStoreDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PosDTO posDTO=posService.getPosDTOById(saveOpenStoreDTO.getPos());
        LoginDTO loginDTO=loginService.loginProcess(saveOpenStoreDTO.getEmployeeId(), saveOpenStoreDTO.getEmployeePw());
        if (loginDTO==null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("아이디와 로그인이 올바르지 않습니다.")));
        }

        EmployeeDTO employeeDTO = employeeService.getEmployeeByLogin(loginDTO);
        session.setAttribute("currentPos", posDTO);

        AmountRecordDTO existAmountRecordDTO = amountRecordService.getExistAmountRecord(storeDTO, posDTO);
        if(existAmountRecordDTO!=null) {
            session.setAttribute("currentRecord", existAmountRecordDTO);
            Long tempAmount = (saveOpenStoreDTO.getTotal()!=0L)?saveOpenStoreDTO.getTotal() : null;
            amountRecordService.updateAmountRecordReopen(existAmountRecordDTO.getId(), tempAmount,employeeDTO);
        }else{
            AmountRecordDTO amountRecordDTO = AmountRecordDTO.builder()
                    .amount(saveOpenStoreDTO.getTotal())
                    .storeDTO(storeDTO)
                    .posDTO(posDTO)
                    .openEmployeeDTO(employeeDTO)
                    .build();
            AmountRecordDTO savedAmountRecordDTO = amountRecordService.createAmountRecord(amountRecordDTO);
            session.setAttribute("currentRecord", savedAmountRecordDTO);
        }

        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @PutMapping("/api/closeStore")
    public ResponseEntity<?> closeStore(@Valid @RequestBody SaveCloseStoreDTO saveCloseStoreDTO, HttpSession session) {
        LoginDTO loginDTO=loginService.loginProcess(saveCloseStoreDTO.getEmployeeId(), saveCloseStoreDTO.getEmployeePw());
        if (loginDTO==null) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("아이디와 로그인이 올바르지 않습니다.")));
        }

        EmployeeDTO employeeDTO = employeeService.getEmployeeByLogin(loginDTO);
        AmountRecordDTO existAmountRecordDTO = amountRecordService.getAmountRecordDTOById(saveCloseStoreDTO.getRecordId());
        PosDTO posDTO = existAmountRecordDTO.getPosDTO();

        Long cash = paymentMethodService.getPaymentPriceByPos(posDTO, existAmountRecordDTO.getRegDate(), 0);
        Long card = paymentMethodService.getPaymentPriceByPos(posDTO, existAmountRecordDTO.getRegDate(), 1);
        amountRecordService.updateAmountRecord(existAmountRecordDTO.getId(), cash+card, saveCloseStoreDTO.getTotal(),employeeDTO);

        session.removeAttribute("currentPos");
        session.removeAttribute("currentRecord");
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
