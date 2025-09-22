package com.project.pos.stock.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.option.dto.OptionDTO;
import com.project.pos.option.dto.queryDTO.OptionSoldOutDTO;
import com.project.pos.option.service.OptionReceiptService;
import com.project.pos.option.service.OptionService;
import com.project.pos.product.dto.ProductDTO;
import com.project.pos.product.dto.queryDTO.ProductSoldOutDTO;
import com.project.pos.product.service.ProductReceiptService;
import com.project.pos.product.service.ProductService;
import com.project.pos.stock.dto.StockDTO;
import com.project.pos.stock.dto.StockMovementDTO;
import com.project.pos.stock.dto.responseDTO.StockHistoryPageDTO;
import com.project.pos.stock.entity.StockMovement;
import com.project.pos.stock.repository.StockMovementRepository;
import com.project.pos.store.service.StoreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockMovementServiceImpl implements StockMovementService {
    private final StockMovementRepository stockMovementRepository;
    private final StoreService storeService;
    private final StockService stockService;
    private final ProductService productService;
    private final OptionService optionService;
    private final ProductReceiptService productReceiptService;
    private final OptionReceiptService optionReceiptService;

    @Override
    public StockMovement getStockMovementById(Long id){
        return stockMovementRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 재고입출고내역 없음"));
    }
    @Override
    public StockMovementDTO getStockMovementDTOById(Long id){
        StockMovement entity=getStockMovementById(id);
        return entityToDto(entity);
    }

    @Override
    public Long createStockMovement(StockMovementDTO dto){
        StockMovement entity=dtoToEntity(dto);
        StockMovement saved=stockMovementRepository.save(entity);

        StockDTO stockDTO = stockService.entityToDto(saved.getStock());
        StockDTO savedStockDTO = stockService.updateStockQuantity(saved.getStock().getId(), saved.getPlusType(), saved.getMovementAmount());
        if(stockDTO.getSoldOut() && !savedStockDTO.getSoldOut()){
            List<ProductSoldOutDTO> productSoldOut = productService.getProductSoldOutList(stockDTO);
            if(productSoldOut!=null){
                for(ProductSoldOutDTO temp : productSoldOut){
                    if(temp.getSoldOutCount()==0L) productService.updateProductSoldOut(temp.getId(), false);
                }
            }
            List<OptionSoldOutDTO> optionSoldOut = optionService.getOptionSoldOutList(stockDTO);
            if(optionSoldOut!=null){
                for(OptionSoldOutDTO temp : optionSoldOut){
                    if(temp.getSoldOutCount()==0L) optionService.updateOptionSoldOut(temp.getId(), false);
                }
            }
        }else if(!stockDTO.getSoldOut() && savedStockDTO.getSoldOut()){
            List<ProductDTO> productSoldOutList = productReceiptService.getProductSoldOutList(stockDTO, false);
            if(productSoldOutList!=null) productSoldOutList.forEach(temp -> productService.updateProductSoldOut(temp.getId(), true));
            List<OptionDTO> optionSoldOutList = optionReceiptService.getOptionSoldOutList(stockDTO, false);
            if(optionSoldOutList!=null) optionSoldOutList.forEach(temp -> optionService.updateOptionSoldOut(temp.getId(), true));
        }

        return saved.getId();
    }

    @Override
    public StockMovement dtoToEntity(StockMovementDTO dto){
        return StockMovement.builder()
                .id(dto.getId())
                .movementAmount(dto.getMovementAmount())
                .presentAmount(dto.getPresentAmount())
                .stockCost(dto.getStockCost())
                .typeValue(dto.getTypeValue())
                .plusType(dto.getPlusType())
                .notes(dto.getNotes())
                .stock(dto.getStockDTO()!=null? stockService.dtoToEntity(dto.getStockDTO()):null)
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public StockMovementDTO entityToDto(StockMovement entity){
        return StockMovementDTO.builder()
                .id(entity.getId())
                .movementAmount(entity.getMovementAmount())
                .presentAmount(entity.getPresentAmount())
                .stockCost(entity.getStockCost())
                .typeValue(entity.getTypeValue())
                .plusType(entity.getPlusType())
                .notes(entity.getNotes())
                .stockDTO(entity.getStock()!=null? stockService.entityToDto(entity.getStock()):null)
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public Page<StockHistoryPageDTO> getStockHistoryPage(PageRequestDTO requestDTO){
        return stockMovementRepository.getStockHistoryPage(requestDTO);
    }
}
