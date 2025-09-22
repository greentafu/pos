package com.project.pos.product.dto.responseDTO;

import com.project.pos.product.dto.ProductCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductCategoryArrangementDTO {
    private List<ProductCategoryDTO> nonArrayList;
    private List<ProductCategoryDTO> arrayList;
}
