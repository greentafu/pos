package com.project.pos.option.repositoryDSL;

import com.project.pos.dslDTO.SoldOutListRequestDTO;
import com.project.pos.option.dto.queryDTO.OptionSoldOutDTO;
import com.project.pos.option.dto.queryDTO.OrderOptionDTO;
import com.project.pos.option.dto.responseDTO.OptionPageDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.option.entity.Option;
import com.project.pos.stock.entity.Stock;
import com.project.pos.store.entity.Store;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OptionRepositoryCustom {
    Page<OptionPageDTO> getOptionPage(PageRequestDTO requestDTO);
    List<Option> getSoldOutOptionList(SoldOutListRequestDTO requestDTO);
    List<OptionSoldOutDTO> getOptionSoldOutCountList(Stock stock);
    List<OrderOptionDTO> getOrderOptionList(Store store);
}
