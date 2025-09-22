package com.project.pos.discount.controller;

import com.project.pos.discount.dto.DiscountCategoryDTO;
import com.project.pos.discount.dto.requestDTO.SaveCategoryArrangementDTO;
import com.project.pos.discount.dto.requestDTO.SaveDiscountCategoryDTO;
import com.project.pos.discount.dto.responseDTO.DiscountCategoryArrangementDTO;
import com.project.pos.discount.dto.responseDTO.DiscountCategoryPageDTO;
import com.project.pos.discount.service.DiscountCategoryService;
import com.project.pos.discount.service.DiscountService;
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
public class DiscountCategoryApiController {
    @Autowired
    private DiscountCategoryService discountCategoryService;
    @Autowired
    private DiscountService discountService;

    @GetMapping("/api/discountCategoryPage")
    public Page<DiscountCategoryPageDTO> getDiscountCategoryPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                                              @RequestParam(value = "searchText", required = false) String searchText, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).searchText(searchText).storeDTO(storeDTO)
                .build();
        return discountCategoryService.getDiscountCategoryPage(requestDTO);
    }
    @GetMapping("/api/getDiscountCategory")
    public DiscountCategoryDTO getDiscountCategory(@RequestParam(value = "id") Long id) {
        return discountCategoryService.getDiscountCategoryDTOById(id);
    }
    @PostMapping("/api/saveDiscountCategory")
    public DiscountCategoryDTO saveDiscountCategory(@Valid @RequestBody SaveDiscountCategoryDTO saveDiscountCategoryDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=saveDiscountCategoryDTO.getId();

        if(tempId==0L){
            DiscountCategoryDTO discountCategoryDTO = DiscountCategoryDTO.builder()
                    .id(null)
                    .name(saveDiscountCategoryDTO.getName())
                    .arrangement(null)
                    .multi(saveDiscountCategoryDTO.getMulti())
                    .deleted(false)
                    .storeDTO(storeDTO)
                    .build();
            return discountCategoryService.createDiscountCategory(discountCategoryDTO);
        }else{
            return discountCategoryService.updateDiscountCategory(tempId, saveDiscountCategoryDTO.getName(), saveDiscountCategoryDTO.getMulti());
        }
    }
    @DeleteMapping("/api/deleteDiscountCategory")
    public ResponseEntity<?> deleteDiscountCategory(@RequestParam(value = "id") Long id) {
        DiscountCategoryDTO discountCategoryDTO=discountCategoryService.getDiscountCategoryDTOById(id);
        Long discountCount = discountService.countDiscountByCategory(discountCategoryDTO);
        if(discountCount!=0){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("해당 항목을 사용중인 할인율이 존재합니다.")));
        }
        discountCategoryService.deleteDiscountCategory(id);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @GetMapping("/api/getDiscountCategoryList")
    public DiscountCategoryArrangementDTO getDiscountCategoryList(HttpSession session){
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<DiscountCategoryDTO> nonArray = discountCategoryService.getDiscountCategoryNonArray(storeDTO);
        List<DiscountCategoryDTO> array = discountCategoryService.getDiscountCategoryArray(storeDTO);
        return new DiscountCategoryArrangementDTO(nonArray, array);
    }
    @PostMapping("/api/saveDiscountCategoryArrangement")
    public ResponseEntity<?> saveDiscountCategoryArrangement(@Valid @RequestBody SaveCategoryArrangementDTO saveCategoryArrangementDTO, HttpSession session){
        List<Long> nonArray = saveCategoryArrangementDTO.getNonArrangementList();
        List<Long> array = saveCategoryArrangementDTO.getArrangementList();

        if(nonArray!=null) {
            nonArray.forEach(temp -> discountCategoryService.updateDiscountCategoryArrangement(temp, null));
        }
        if(array!=null) {
            for(int i=1; i<array.size()+1; i++){
                discountCategoryService.updateDiscountCategoryArrangement(array.get(i-1), Long.valueOf(i+""));
            }
        }
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
