package com.project.pos.option.dto.responseDTO;

import com.project.pos.option.dto.OptionDTO;
import com.project.pos.option.dto.OptionReceiptDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OptionDetailDTO {
    private OptionDTO optionDTO;
    private List<OptionReceiptDTO> receiptList;
}
