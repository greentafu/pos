package com.project.pos.store.controller;

import com.project.pos.employee.service.JobTitleService;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.MailDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.dto.requestDTO.SaveMailDTO;
import com.project.pos.store.dto.responseDTO.MailPageDTO;
import com.project.pos.store.service.MailService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class MailApiController {
    @Autowired
    private MailService mailService;
    @Autowired
    private JobTitleService jobTitleService;

    @GetMapping("/api/mailPage")
    public Page<MailPageDTO> mailPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                      @RequestParam(value = "searchText", required = false) String searchText, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).searchText(searchText).storeDTO(storeDTO)
                .build();
        return mailService.getMailPage(requestDTO);
    }
    @GetMapping("/api/getMail")
    public MailDTO getMail(@RequestParam(value = "id") Long id) {
        return mailService.getMailDTOById(id);
    }
    @PostMapping("/api/saveMail")
    public ResponseEntity<?> saveMail(@Valid @RequestBody SaveMailDTO saveMailDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=saveMailDTO.getId();

        MailDTO mailDTO = MailDTO.builder()
                .id(tempId==0L? null:tempId)
                .title(saveMailDTO.getTitle())
                .notes(saveMailDTO.getNotes())
                .content(saveMailDTO.getContent())
                .storeDTO(storeDTO)
                .build();
        mailService.createMail(mailDTO);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @DeleteMapping("/api/deleteMail")
    public void deleteMail(@RequestParam(value = "id") Long id) {
        mailService.deleteMail(id);
    }
}
