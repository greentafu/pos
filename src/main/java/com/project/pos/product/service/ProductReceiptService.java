package com.project.pos.product.service;

import com.project.pos.product.dto.ProductDTO;
import com.project.pos.product.dto.ProductReceiptDTO;
import com.project.pos.product.dto.queryDTO.ProductQuantityDTO;
import com.project.pos.product.entity.ProductReceipt;
import com.project.pos.stock.dto.StockDTO;

import java.util.List;

public interface ProductReceiptService {
    ProductReceipt getProductReceiptById(Long jobTitleKey, Long screenKey);
    ProductReceiptDTO getProductReceiptDTOById(Long jobTitleKey, Long screenKey);

    ProductReceiptDTO createProductReceipt(ProductReceiptDTO dto);
    void deleteProductReceiptByProductKey(Long productKey);

    ProductReceipt dtoToEntity(ProductReceiptDTO dto);
    ProductReceiptDTO entityToDto(ProductReceipt entity);

    Long countProductReceiptByStock(StockDTO stockDTO);
    List<ProductQuantityDTO> getProductListByStock(StockDTO stockDTO);
    List<ProductDTO> getProductSoldOutList(StockDTO stockDTO, Boolean soldOut);
    List<ProductReceiptDTO> getProductReceiptByProduct(ProductDTO productDTO);
}
