package com.project.pos.discount.dto.responseDTO;

import com.project.pos.discount.dto.DiscountCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DiscountCategoryArrangementDTO {
    private List<DiscountCategoryDTO> nonArrayList;
    private List<DiscountCategoryDTO> arrayList;
}
