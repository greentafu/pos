package com.project.pos.option.service;

import com.project.pos.dslDTO.SoldOutListRequestDTO;
import com.project.pos.option.dto.queryDTO.OptionSoldOutDTO;
import com.project.pos.option.dto.queryDTO.OrderOptionDTO;
import com.project.pos.option.dto.responseDTO.OptionPageDTO;
import com.project.pos.option.dto.OptionCategoryDTO;
import com.project.pos.option.dto.OptionDTO;
import com.project.pos.option.entity.Option;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.product.dto.queryDTO.ProductSoldOutDTO;
import com.project.pos.stock.dto.StockDTO;
import com.project.pos.store.dto.StoreDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OptionService {
    Option getOptionById(Long id);
    OptionDTO getOptionDTOById(Long id);

    OptionDTO createOption(OptionDTO dto);
    Long deleteOption(Long id);
    OptionDTO updateOption(Long id, Long number, String name, String displayName, Long optionPrice, Long optionCost, OptionCategoryDTO optionCategoryDTO);
    Long updateOptionArrangement(Long id, Long arrangement);
    Long updateOptionSoldOut(Long id, Boolean soldOut);
    Long updateOptionCost(Long id, Long difference);

    Option dtoToEntity(OptionDTO dto);
    OptionDTO entityToDto(Option entity);

    Long countOptionByNumber(StoreDTO storeDTO, Long number);
    Long countOptionByCategory(OptionCategoryDTO dto);
    List<OptionDTO> getOptionNonArray(StoreDTO storeDTO, Long searchCategory);
    List<OptionDTO> getOptionArray(StoreDTO storeDTO, Long searchCategory);
    List<OptionSoldOutDTO> getOptionSoldOutList(StockDTO stockDTO);
    List<OptionDTO> getOptionSoldOut(SoldOutListRequestDTO requestDTO);
    List<OrderOptionDTO> getOrderOptionList(StoreDTO storeDTO);
    Page<OptionPageDTO> getOptionPage(PageRequestDTO requestDTO);
}
