package com.project.pos.option.controller;

import com.project.pos.option.dto.OptionCategoryDTO;
import com.project.pos.option.dto.requestDTO.SaveCategoryArrangementDTO;
import com.project.pos.option.dto.requestDTO.SaveOptionCategoryDTO;
import com.project.pos.option.dto.responseDTO.OptionCategoryArrangementDTO;
import com.project.pos.option.dto.responseDTO.OptionCategoryPageDTO;
import com.project.pos.option.service.OptionCategoryService;
import com.project.pos.option.service.OptionService;
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

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class OptionCategoryApiController {
    @Autowired
    private OptionCategoryService optionCategoryService;
    @Autowired
    private OptionService optionService;

    @GetMapping("/api/optionCategoryPage")
    public Page<OptionCategoryPageDTO> getOptionCategoryPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                                              @RequestParam(value = "searchText", required = false) String searchText, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).searchText(searchText).storeDTO(storeDTO)
                .build();
        return optionCategoryService.getOptionCategoryPage(requestDTO);
    }
    @GetMapping("/api/getOptionCategory")
    public OptionCategoryDTO getOptionCategory(@RequestParam(value = "id") Long id) {
        return optionCategoryService.getOptionCategoryDTOById(id);
    }
    @PostMapping("/api/saveOptionCategory")
    public OptionCategoryDTO saveOptionCategory(@Valid @RequestBody SaveOptionCategoryDTO saveOptionCategoryDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=saveOptionCategoryDTO.getId();

        if(tempId==0L){
            OptionCategoryDTO optionCategoryDTO = OptionCategoryDTO.builder()
                    .id(null)
                    .name(saveOptionCategoryDTO.getName())
                    .arrangement(null)
                    .multi(saveOptionCategoryDTO.getMulti())
                    .deleted(false)
                    .storeDTO(storeDTO)
                    .build();
            return optionCategoryService.createOptionCategory(optionCategoryDTO);
        }else{
            return optionCategoryService.updateOptionCategory(tempId, saveOptionCategoryDTO.getName(), saveOptionCategoryDTO.getMulti());
        }
    }
    @DeleteMapping("/api/deleteOptionCategory")
    public ResponseEntity<?> deleteOptionCategory(@RequestParam(value = "id") Long id) {
        OptionCategoryDTO optionCategoryDTO=optionCategoryService.getOptionCategoryDTOById(id);
        Long optionCount = optionService.countOptionByCategory(optionCategoryDTO);
        if(optionCount!=0){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("해당 항목을 사용중인 옵션이 존재합니다.")));
        }
        optionCategoryService.deleteOptionCategory(id);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @GetMapping("/api/getOptionCategoryList")
    public OptionCategoryArrangementDTO getOptionCategoryList(HttpSession session){
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<OptionCategoryDTO> nonArray = optionCategoryService.getOptionCategoryNonArray(storeDTO);
        List<OptionCategoryDTO> array = optionCategoryService.getOptionCategoryArray(storeDTO);
        return new OptionCategoryArrangementDTO(nonArray, array);
    }
    @PostMapping("/api/saveOptionCategoryArrangement")
    public ResponseEntity<?> saveOptionCategoryArrangement(@Valid @RequestBody SaveCategoryArrangementDTO saveCategoryArrangementDTO, HttpSession session){
        List<Long> nonArray = saveCategoryArrangementDTO.getNonArrangementList();
        List<Long> array = saveCategoryArrangementDTO.getArrangementList();

        if(nonArray!=null) {
            nonArray.forEach(temp -> optionCategoryService.updateOptionCategoryArrangement(temp, null));
        }
        if(array!=null) {
            for(int i=1; i<array.size()+1; i++){
                optionCategoryService.updateOptionCategoryArrangement(array.get(i-1), Long.valueOf(i+""));
            }
        }
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
