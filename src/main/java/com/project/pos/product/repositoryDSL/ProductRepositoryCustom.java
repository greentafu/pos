package com.project.pos.product.repositoryDSL;

import com.project.pos.dslDTO.SoldOutListRequestDTO;
import com.project.pos.product.dto.queryDTO.ProductSoldOutDTO;
import com.project.pos.product.entity.Product;
import com.project.pos.product.dto.responseDTO.ProductPageDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.stock.entity.Stock;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductRepositoryCustom {
    Page<ProductPageDTO> getProductPage(PageRequestDTO requestDTO);
    List<Product> getSoldOutProductList(SoldOutListRequestDTO requestDTO);
    List<ProductSoldOutDTO> getProductSoldOutCountList(Stock stock);
}
