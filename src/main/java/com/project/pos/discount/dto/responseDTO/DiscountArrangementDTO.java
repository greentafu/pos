package com.project.pos.discount.dto.responseDTO;

import com.project.pos.discount.dto.DiscountDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DiscountArrangementDTO {
    private List<DiscountDTO> nonArrayList;
    private List<DiscountDTO> arrayList;
}
