package com.project.pos.option.controller;

import com.project.pos.option.dto.OptionCategoryDTO;
import com.project.pos.option.dto.OptionDTO;
import com.project.pos.option.dto.OptionReceiptDTO;
import com.project.pos.option.dto.requestDTO.ReceiptDTO;
import com.project.pos.option.dto.requestDTO.SaveArrangementDTO;
import com.project.pos.option.dto.requestDTO.SaveOptionDTO;
import com.project.pos.option.dto.responseDTO.OptionArrangementDTO;
import com.project.pos.option.dto.responseDTO.OptionDetailDTO;
import com.project.pos.option.dto.responseDTO.OptionPageDTO;
import com.project.pos.option.service.OptionCategoryService;
import com.project.pos.option.service.OptionReceiptService;
import com.project.pos.option.service.OptionService;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.stock.dto.StockDTO;
import com.project.pos.stock.service.StockService;
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
public class OptionApiController {
    @Autowired
    private OptionService optionService;
    @Autowired
    private OptionCategoryService optionCategoryService;
    @Autowired
    private StockService stockService;
    @Autowired
    private OptionReceiptService optionReceiptService;

    @GetMapping("/api/optionPage")
    public Page<OptionPageDTO> optionPage(@RequestParam(value = "page") int page, @RequestParam(value = "size") int size,
                                          @RequestParam(value = "searchText", required = false) String searchText,
                                          @RequestParam(value = "searchCategory", required = false) Long searchCategory, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        PageRequestDTO requestDTO= PageRequestDTO.builder()
                .page(page).size(size).searchText(searchText).searchCategory(searchCategory).storeDTO(storeDTO)
                .build();
        return optionService.getOptionPage(requestDTO);
    }
    @GetMapping("/api/getOption")
    public OptionDetailDTO getOption(@RequestParam(value = "id") Long id) {
        OptionDTO optionDTO = optionService.getOptionDTOById(id);
        List<OptionReceiptDTO> receiptList = optionReceiptService.getOptionReceiptByOption(optionDTO);
        return new OptionDetailDTO(optionDTO, receiptList);
    }
    @PostMapping("/api/saveOption")
    public ResponseEntity<?> saveOption(@Valid @RequestBody SaveOptionDTO saveOptionDTO, HttpSession session) {
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        Long tempId=saveOptionDTO.getId();

        OptionCategoryDTO categoryDTO = optionCategoryService.getOptionCategoryDTOById(saveOptionDTO.getOptionCategory());

        Long tempNumber = optionService.countOptionByNumber(storeDTO, saveOptionDTO.getNumber());
        if(tempNumber!=null && !tempNumber.equals(tempId)){
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("errors", List.of("해당 번호를 사용중인 옵션이 존재합니다.")));
        }

        Long tempPrice = Long.parseLong(saveOptionDTO.getOptionPrice().replace(",", ""));
        Long tempCost = Long.parseLong(saveOptionDTO.getOptionCost().replace(",", ""));
        OptionDTO savedOptionDTO=null;
        if(tempId==0L){
            OptionDTO optionDTO = OptionDTO.builder()
                    .id(null)
                    .number(saveOptionDTO.getNumber())
                    .name(saveOptionDTO.getName())
                    .displayName(saveOptionDTO.getDisplayName())
                    .optionPrice(tempPrice)
                    .optionCost(tempCost)
                    .soldOutType(saveOptionDTO.getSoldOutType())
                    .soldOut(false)
                    .deleted(false)
                    .storeDTO(storeDTO)
                    .optionCategoryDTO(categoryDTO)
                    .build();
            savedOptionDTO = optionService.createOption(optionDTO);
        }else{
            savedOptionDTO = optionService.updateOption(tempId, saveOptionDTO.getNumber(), saveOptionDTO.getName(),
                    saveOptionDTO.getDisplayName(), tempPrice, tempCost, categoryDTO);
            optionReceiptService.deleteOptionReceiptByOptionKey(savedOptionDTO.getId());
        }

        List<ReceiptDTO> receiptList = saveOptionDTO.getReceiptList();
        for (ReceiptDTO receiptDTO : receiptList) {
            StockDTO stockDTO = stockService.getStockDTOById(receiptDTO.getId());
            if(savedOptionDTO.getSoldOutType() && stockDTO.getSoldOut()) optionService.updateOptionSoldOut(savedOptionDTO.getId(), true);

            Long quantity = Long.parseLong(receiptDTO.getQuantity().replace(",", ""));
            OptionReceiptDTO optionReceiptDTO = OptionReceiptDTO.builder()
                    .optionId(savedOptionDTO.getId())
                    .stockId(receiptDTO.getId())
                    .quantity(quantity)
                    .stockDTO(stockDTO)
                    .optionDTO(savedOptionDTO)
                    .build();
            optionReceiptService.createOptionReceipt(optionReceiptDTO);
        }
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @DeleteMapping("/api/deleteOption")
    public ResponseEntity<?> deleteOption(@RequestParam(value = "id") Long id) {
        OptionDTO optionDTO = optionService.getOptionDTOById(id);
        optionReceiptService.deleteOptionReceiptByOptionKey(optionDTO.getId());
        optionService.deleteOption(optionDTO.getId());
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
    @GetMapping("/api/getOptionList")
    public OptionArrangementDTO getOptionList(@RequestParam(value = "searchCategory") Long searchCategory, HttpSession session){
        StoreDTO storeDTO=(StoreDTO) session.getAttribute("currentStore");
        List<OptionDTO> nonArray = optionService.getOptionNonArray(storeDTO, searchCategory);
        List<OptionDTO> array = optionService.getOptionArray(storeDTO, searchCategory);
        return new OptionArrangementDTO(nonArray, array);
    }
    @PostMapping("/api/saveOptionArrangement")
    public ResponseEntity<?> saveOptionArrangement(@Valid @RequestBody SaveArrangementDTO saveArrangementDTO, HttpSession session){
        List<Long> nonArray = saveArrangementDTO.getNonArrangementList();
        List<Long> array = saveArrangementDTO.getArrangementList();

        if(nonArray!=null) {
            nonArray.forEach(temp -> optionService.updateOptionArrangement(temp, null));
        }
        if(array!=null) {
            for(int i=1; i<array.size()+1; i++){
                optionService.updateOptionArrangement(array.get(i-1), Long.valueOf(i+""));
            }
        }
        return ResponseEntity.ok(Map.of("message", "Success"));
    }
}
