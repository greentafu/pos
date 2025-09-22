package com.project.pos.product.service;

import com.project.pos.product.dto.ProductDTO;
import com.project.pos.product.dto.ProductReceiptDTO;
import com.project.pos.product.dto.queryDTO.ProductQuantityDTO;
import com.project.pos.product.entity.Product;
import com.project.pos.product.entity.ProductReceipt;
import com.project.pos.product.entity.ProductReceiptID;
import com.project.pos.product.repository.ProductReceiptRepository;
import com.project.pos.product.service.ProductService;
import com.project.pos.stock.dto.StockDTO;
import com.project.pos.stock.entity.Stock;
import com.project.pos.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReceiptServiceImpl implements ProductReceiptService {
    private final ProductReceiptRepository productReceiptRepository;
    private final ProductService productService;
    private final StockService stockService;

    @Override
    public ProductReceipt getProductReceiptById(Long productKey, Long stockKey){
        return productReceiptRepository.findById(new ProductReceiptID(productKey, stockKey))
                .orElse(null);
    }
    @Override
    public ProductReceiptDTO getProductReceiptDTOById(Long productKey, Long stockKey){
        ProductReceipt entity=getProductReceiptById(productKey, stockKey);
        return entity!=null? entityToDto(entity):null;
    }

    @Override
    public ProductReceiptDTO createProductReceipt(ProductReceiptDTO dto){
        ProductReceipt entity=dtoToEntity(dto);
        ProductReceipt saved=productReceiptRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    public void deleteProductReceiptByProductKey(Long productKey){
        productReceiptRepository.deleteProductReceiptByProductKey(productKey);
    }

    @Override
    public ProductReceipt dtoToEntity(ProductReceiptDTO dto){
        return ProductReceipt.builder()
                .id(new ProductReceiptID(dto.getProductId(), dto.getStockId()))
                .quantity(dto.getQuantity())
                .product(dto.getProductDTO()!=null? productService.dtoToEntity(dto.getProductDTO()):null)
                .stock(dto.getStockDTO()!=null? stockService.dtoToEntity(dto.getStockDTO()):null)
                .build();
    }
    @Override
    public ProductReceiptDTO entityToDto(ProductReceipt entity){
        return ProductReceiptDTO.builder()
                .productId(entity.getId().getProductKey())
                .stockId(entity.getId().getStockKey())
                .quantity(entity.getQuantity())
                .productDTO(entity.getProduct()!=null? productService.entityToDto(entity.getProduct()):null)
                .stockDTO(entity.getStock()!=null? stockService.entityToDto(entity.getStock()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public Long countProductReceiptByStock(StockDTO stockDTO){
        Stock stock=stockService.dtoToEntity(stockDTO);
        return productReceiptRepository.countProductReceiptByStock(stock);
    }
    @Override
    public List<ProductQuantityDTO> getProductListByStock(StockDTO stockDTO){
        Stock stock = stockService.dtoToEntity(stockDTO);
        return productReceiptRepository.getProductListByStock(stock);
    }
    @Override
    public List<ProductDTO> getProductSoldOutList(StockDTO stockDTO, Boolean soldOut){
        Stock stock = stockService.dtoToEntity(stockDTO);
        List<Product> entityList = productReceiptRepository.getProductSoldOut(stock, soldOut);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(productService::entityToDto).toList();
    }
    @Override
    public List<ProductReceiptDTO> getProductReceiptByProduct(ProductDTO productDTO){
        Product product = productService.dtoToEntity(productDTO);
        List<ProductReceipt> entityList = productReceiptRepository.getProductReceiptByProduct(product);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
}
