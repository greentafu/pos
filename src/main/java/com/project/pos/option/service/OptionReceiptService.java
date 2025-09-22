package com.project.pos.option.service;

import com.project.pos.option.dto.OptionDTO;
import com.project.pos.option.dto.OptionReceiptDTO;
import com.project.pos.option.dto.queryDTO.OptionQuantityDTO;
import com.project.pos.option.entity.OptionReceipt;
import com.project.pos.stock.dto.StockDTO;

import java.util.List;

public interface OptionReceiptService {
    OptionReceipt getOptionReceiptById(Long jobTitleKey, Long screenKey);
    OptionReceiptDTO getOptionReceiptDTOById(Long jobTitleKey, Long screenKey);

    OptionReceiptDTO createOptionReceipt(OptionReceiptDTO dto);
    void deleteOptionReceiptByOptionKey(Long optionKey);

    OptionReceipt dtoToEntity(OptionReceiptDTO dto);
    OptionReceiptDTO entityToDto(OptionReceipt entity);

    Long countOptionReceiptByStock(StockDTO stockDTO);
    List<OptionQuantityDTO> getOptionListByStock(StockDTO stockDTO);
    List<OptionDTO> getOptionSoldOutList(StockDTO stockDTO, Boolean soldOut);
    List<OptionReceiptDTO> getOptionReceiptByOption(OptionDTO optionDTO);
}
