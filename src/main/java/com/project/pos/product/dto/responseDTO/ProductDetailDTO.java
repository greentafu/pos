package com.project.pos.product.dto.responseDTO;

import com.project.pos.product.dto.ProductDTO;
import com.project.pos.product.dto.ProductReceiptDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductDetailDTO {
    private ProductDTO productDTO;
    private List<ProductReceiptDTO> receiptList;
}
