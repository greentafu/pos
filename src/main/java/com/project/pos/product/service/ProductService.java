package com.project.pos.product.service;

import com.project.pos.dslDTO.SoldOutListRequestDTO;
import com.project.pos.product.dto.ProductDTO;
import com.project.pos.product.dto.ProductCategoryDTO;
import com.project.pos.product.dto.queryDTO.ProductSoldOutDTO;
import com.project.pos.product.dto.responseDTO.ProductPageDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.product.entity.Product;
import com.project.pos.stock.dto.StockDTO;
import com.project.pos.store.dto.StoreDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Product getProductById(Long id);
    ProductDTO getProductDTOById(Long id);

    ProductDTO createProduct(ProductDTO dto);
    Long deleteProduct(Long id);
    ProductDTO updateProduct(Long id, Long number, String name, String displayName, Long productPrice, Long productCost, ProductCategoryDTO productCategoryDTO);
    Long updateProductSoldOut(Long id, Boolean soldOut);
    Long updateProductCost(Long id, Long difference);

    Product dtoToEntity(ProductDTO dto);
    ProductDTO entityToDto(Product entity);

    Long countProductByNumber(StoreDTO storeDTO, Long number);
    Long countProductByCategory(ProductCategoryDTO dto);
    List<ProductSoldOutDTO> getProductSoldOutList(StockDTO stockDTO);
    List<ProductDTO> getProductSoldOut(SoldOutListRequestDTO requestDTO);
    Page<ProductPageDTO> getProductPage(PageRequestDTO requestDTO);
}
