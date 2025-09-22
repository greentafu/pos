package com.project.pos.product.service;

import com.project.pos.dslDTO.SoldOutListRequestDTO;
import com.project.pos.option.entity.Option;
import com.project.pos.product.dto.ProductDTO;
import com.project.pos.product.dto.queryDTO.ProductSoldOutDTO;
import com.project.pos.product.entity.Product;
import com.project.pos.product.dto.responseDTO.ProductPageDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.product.dto.ProductCategoryDTO;
import com.project.pos.product.entity.ProductCategory;
import com.project.pos.product.repository.ProductRepository;
import com.project.pos.stock.dto.StockDTO;
import com.project.pos.stock.entity.Stock;
import com.project.pos.stock.service.StockService;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.entity.Store;
import com.project.pos.store.service.StoreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final StoreService storeService;
    private final ProductCategoryService productCategoryService;
    private final StockService stockService;

    @Override
    public Product getProductById(Long id){
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 상품 없음"));
    }
    @Override
    public ProductDTO getProductDTOById(Long id){
        Product entity=getProductById(id);
        return entityToDto(entity);
    }

    @Override
    public ProductDTO createProduct(ProductDTO dto){
        Product entity=dtoToEntity(dto);
        Product saved=productRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public Long deleteProduct(Long id){
        Product entity = getProductById(id);
        entity.setDeleted(true);
        return id;
    }
    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, Long number, String name, String displayName,
                                  Long productPrice, Long productCost, ProductCategoryDTO productCategoryDTO){
        ProductCategory productCategory = productCategoryService.dtoToEntity(productCategoryDTO);
        Product entity = getProductById(id);
        entity.setNumber(number);
        entity.setName(name);
        if(displayName==null) entity.setDisplayName(name);
        else entity.setDisplayName(displayName);
        entity.setProductPrice(productPrice);
        entity.setProductCost(productCost);
        entity.setProductCategory(productCategory);
        return entityToDto(entity);
    }
    @Override
    @Transactional
    public Long updateProductSoldOut(Long id, Boolean soldOut){
        Product entity = getProductById(id);
        entity.setSoldOut(soldOut);
        return id;
    }
    @Override
    @Transactional
    public Long updateProductCost(Long id, Long difference){
        Product entity = getProductById(id);
        entity.setProductCost(entity.getProductCost()+difference);
        return id;
    }

    @Override
    public Product dtoToEntity(ProductDTO dto){
        return Product.builder()
                .id(dto.getId())
                .number(dto.getNumber())
                .name(dto.getName())
                .displayName(dto.getDisplayName())
                .productPrice(dto.getProductPrice())
                .productCost(dto.getProductCost())
                .soldOutType(dto.getSoldOutType())
                .soldOut(dto.getSoldOut())
                .deleted(dto.getDeleted())
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .productCategory(dto.getProductCategoryDTO()!=null? productCategoryService.dtoToEntity(dto.getProductCategoryDTO()):null)
                .build();
    }
    @Override
    public ProductDTO entityToDto(Product entity){
        return ProductDTO.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .name(entity.getName())
                .displayName(entity.getDisplayName())
                .productPrice(entity.getProductPrice())
                .productCost(entity.getProductCost())
                .soldOutType(entity.getSoldOutType())
                .soldOut(entity.getSoldOut())
                .deleted(entity.getDeleted())
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .productCategoryDTO(entity.getProductCategory()!=null? productCategoryService.entityToDto(entity.getProductCategory()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public Long countProductByNumber(StoreDTO storeDTO, Long number){
        Store store = storeService.dtoToEntity(storeDTO);
        return productRepository.countProductByNumber(store, number);
    }
    @Override
    public Long countProductByCategory(ProductCategoryDTO dto){
        ProductCategory category = productCategoryService.dtoToEntity(dto);
        return productRepository.countProductByCategory(category);
    }
    @Override
    public List<ProductSoldOutDTO> getProductSoldOutList(StockDTO stockDTO){
        Stock stock = stockService.dtoToEntity(stockDTO);
        return productRepository.getProductSoldOutCountList(stock);
    }
    @Override
    public List<ProductDTO> getProductSoldOut(SoldOutListRequestDTO requestDTO){
        List<Product> entityList = productRepository.getSoldOutProductList(requestDTO);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public Page<ProductPageDTO> getProductPage(PageRequestDTO requestDTO){
        return productRepository.getProductPage(requestDTO);
    }
}
