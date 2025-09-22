package com.project.pos.discount.controller;

import com.project.pos.discount.dto.DiscountDTO;
import com.project.pos.discount.dto.requestDTO.SaveArrangementDTO;
import com.project.pos.discount.dto.responseDTO.DiscountArrangementDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.discount.dto.DiscountCategoryDTO;
import com.project.pos.discount.dto.requestDTO.SaveDiscountDTO;
import com.project.pos.discount.dto.responseDTO.DiscountPageDTO;
import com.project.pos.discount.service.DiscountCategoryService;
import com.project.pos.discount.service.DiscountService;
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
public class DiscountApiController {
    @Autowired
    private DiscountService discountService;
    @Autowired
    private DiscountCategoryService discountCategoryService;

    @GetMapping("/api/discountPage")
    public Page<DiscountPageDTO> discountPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                        @RequestParam(value = "searchText", required = false) String searchText,
                                        @RequestParam(value = "searchCategory", required = false) Long searchCategory ,HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).searchText(searchText).searchCategory(searchCategory).storeDTO(storeDTO)
                .build();
        return discountService.getDiscountPage(requestDTO);
    }
    @GetMapping("/api/getDiscount")
    public DiscountDTO getDiscount(@RequestParam(value = "id") Long id) {
        return discountService.getDiscountDTOById(id);
    }
    @PostMapping("/api/saveDiscount")
    public ResponseEntity<?> saveDiscount(@Valid @RequestBody SaveDiscountDTO saveDiscountDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=saveDiscountDTO.getId();

        Long price = Long.parseLong(saveDiscountDTO.getPrice().replace(",", ""));
        DiscountCategoryDTO discountCategoryDTO=discountCategoryService.getDiscountCategoryDTOById(saveDiscountDTO.getCategory());
        if(tempId==0L){
            DiscountDTO discountDTO = DiscountDTO.builder()
                    .id(null)
                    .number(saveDiscountDTO.getNumber())
                    .name(saveDiscountDTO.getName())
                    .displayName(saveDiscountDTO.getDisplayName())
                    .discountType(saveDiscountDTO.getDiscountType())
                    .typeValue(saveDiscountDTO.getTypeValue())
                    .discountPrice(price)
                    .deleted(false)
                    .storeDTO(storeDTO)
                    .discountCategoryDTO(discountCategoryDTO)
                    .build();
            discountService.createDiscount(discountDTO);
        }else{
            discountService.updateDiscount(tempId, saveDiscountDTO.getNumber(), saveDiscountDTO.getName(),
                    saveDiscountDTO.getDisplayName(), saveDiscountDTO.getDiscountType(),
                    saveDiscountDTO.getTypeValue(), price, discountCategoryDTO);
        }
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @DeleteMapping("/api/deleteDiscount")
    public ResponseEntity<?> deleteDiscount(@RequestParam(value = "id") Long id) {
        discountService.deleteDiscount(id);
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @GetMapping("/api/getDiscountList")
    public DiscountArrangementDTO getDiscountList(@RequestParam(value = "searchCategory") Long searchCategory, HttpSession session){
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<DiscountDTO> nonArray = discountService.getDiscountNonArray(storeDTO, searchCategory);
        List<DiscountDTO> array = discountService.getDiscountArray(storeDTO, searchCategory);
        return new DiscountArrangementDTO(nonArray, array);
    }
    @PostMapping("/api/saveDiscountArrangement")
    public ResponseEntity<?> saveDiscountArrangement(@Valid @RequestBody SaveArrangementDTO saveArrangementDTO, HttpSession session){
        List<Long> nonArray = saveArrangementDTO.getNonArrangementList();
        List<Long> array = saveArrangementDTO.getArrangementList();

        if(nonArray!=null) {
            nonArray.forEach(temp -> discountService.updateDiscountArrangement(temp, null));
        }
        if(array!=null) {
            for(int i=1; i<array.size()+1; i++){
                discountService.updateDiscountArrangement(array.get(i-1), Long.valueOf(i+""));
            }
        }
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
