package com.project.pos.stock.dto.responseDTO;

import com.project.pos.option.dto.OptionDTO;
import com.project.pos.product.dto.ProductDTO;
import com.project.pos.stock.dto.StockDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SoldOutDTO {
    private List<OptionDTO> optionList;
    private List<ProductDTO> productList;
    private List<StockDTO> stockList;
}
