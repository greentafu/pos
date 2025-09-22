package com.project.pos.product.service;

import com.project.pos.product.dto.ProductBtnDTO;
import com.project.pos.product.dto.ProductDTO;
import com.project.pos.product.dto.ProductWithBtnDTO;
import com.project.pos.product.entity.Product;
import com.project.pos.product.entity.ProductBtn;
import com.project.pos.product.repository.ProductBtnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductBtnServiceImpl implements ProductBtnService {
    private final ProductBtnRepository productBtnRepository;
    private final ProductService productService;

    @Override
    public ProductBtn getProductBtnById(Long id){
        return productBtnRepository.findById(id).orElse(null);
    }
    @Override
    public ProductBtnDTO getProductBtnDTOById(Long id){
        ProductBtn entity=getProductBtnById(id);
        if(entity==null) return null;
        return entityToDto(entity);
    }

    @Override
    public ProductBtnDTO createProductBtn(ProductBtnDTO dto){
        ProductBtn entity=dtoToEntity(dto);
        ProductBtn saved=productBtnRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public Long deleteProductBtn(Long id){
        productBtnRepository.deleteById(id);
        return id;
    }
    @Override
    @Transactional
    public void deleteProductBtnByProduct(ProductDTO productDTO){
        Product product = productService.dtoToEntity(productDTO);
        productBtnRepository.deleteProductBtnByProduct(product);
    }
    @Override
    @Transactional
    public ProductBtnDTO updateProductBtn(Long id, Integer page, Integer index, Integer color, Integer size){
        ProductBtn entity = getProductBtnById(id);
        entity.setPage(page);
        entity.setIndexValue(index);
        entity.setColor(color);
        entity.setSize(size);
        return entityToDto(entity);
    }

    @Override
    public ProductBtn dtoToEntity(ProductBtnDTO dto){
        return ProductBtn.builder()
                .id(dto.getId())
                .page(dto.getPage())
                .indexValue(dto.getIndexValue())
                .size(dto.getSize())
                .color(dto.getColor())
                .product(dto.getProductDTO()!=null? productService.dtoToEntity(dto.getProductDTO()):null)
                .build();
    }
    @Override
    public ProductBtnDTO entityToDto(ProductBtn entity){
        return ProductBtnDTO.builder()
                .id(entity.getProduct()!=null? entity.getProduct().getId():null)
                .page(entity.getPage())
                .indexValue(entity.getIndexValue())
                .size(entity.getSize())
                .color(entity.getColor())
                .productDTO(entity.getProduct()!=null? productService.entityToDto(entity.getProduct()):null)
                .build();
    }

    @Override
    public ProductBtnDTO getProductBtnByProduct(ProductDTO productDTO){
        Product product = productService.dtoToEntity(productDTO);
        ProductBtn entity = productBtnRepository.getProductBtnByProduct(product);
        if(entity==null) return null;
        return entityToDto(entity);
    }
    @Override
    public ProductWithBtnDTO getProductWithBtn(Long id){
        ProductDTO productDTO = productService.getProductDTOById(id);
        ProductBtnDTO btnDTO = getProductBtnDTOById(id);
        return new ProductWithBtnDTO(productDTO, btnDTO);
    }
    @Override
    public List<ProductBtnDTO> getProductBtnList(Long category){
        List<ProductBtn> entityList = productBtnRepository.getProductBtnList(category);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<Long> getProductBtnIdList(Long category){
        List<Long> longList = productBtnRepository.getProductBtnIdList(category);
        if(longList.isEmpty()) return null;
        return longList;
    }
}
