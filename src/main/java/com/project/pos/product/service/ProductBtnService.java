package com.project.pos.product.service;

import com.project.pos.product.dto.ProductBtnDTO;
import com.project.pos.product.dto.ProductDTO;
import com.project.pos.product.dto.ProductWithBtnDTO;
import com.project.pos.product.entity.ProductBtn;

import java.util.List;

public interface ProductBtnService {
    ProductBtn getProductBtnById(Long id);
    ProductBtnDTO getProductBtnDTOById(Long id);

    ProductBtnDTO createProductBtn(ProductBtnDTO dto);
    Long deleteProductBtn(Long id);
    void deleteProductBtnByProduct(ProductDTO productDTO);
    ProductBtnDTO updateProductBtn(Long id, Integer page, Integer index, Integer color, Integer size);

    ProductBtn dtoToEntity(ProductBtnDTO dto);
    ProductBtnDTO entityToDto(ProductBtn entity);

    ProductBtnDTO getProductBtnByProduct(ProductDTO productDTO);
    ProductWithBtnDTO getProductWithBtn(Long id);
    List<ProductBtnDTO> getProductBtnList(Long category);
    List<Long> getProductBtnIdList(Long category);
}
