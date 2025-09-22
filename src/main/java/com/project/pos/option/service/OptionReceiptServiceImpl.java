package com.project.pos.option.service;

import com.project.pos.option.dto.OptionDTO;
import com.project.pos.option.dto.OptionReceiptDTO;
import com.project.pos.option.dto.queryDTO.OptionQuantityDTO;
import com.project.pos.option.entity.Option;
import com.project.pos.option.entity.OptionReceipt;
import com.project.pos.option.entity.OptionReceiptID;
import com.project.pos.option.repository.OptionReceiptRepository;
import com.project.pos.stock.dto.StockDTO;
import com.project.pos.stock.entity.Stock;
import com.project.pos.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionReceiptServiceImpl implements OptionReceiptService {
    private final OptionReceiptRepository optionReceiptRepository;
    private final OptionService optionService;
    private final StockService stockService;

    @Override
    public OptionReceipt getOptionReceiptById(Long optionKey, Long stockKey){
        return optionReceiptRepository.findById(new OptionReceiptID(optionKey, stockKey))
                .orElse(null);
    }
    @Override
    public OptionReceiptDTO getOptionReceiptDTOById(Long optionKey, Long stockKey){
        OptionReceipt entity=getOptionReceiptById(optionKey, stockKey);
        return entity!=null? entityToDto(entity):null;
    }

    @Override
    public OptionReceiptDTO createOptionReceipt(OptionReceiptDTO dto){
        OptionReceipt entity=dtoToEntity(dto);
        OptionReceipt saved=optionReceiptRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    public void deleteOptionReceiptByOptionKey(Long optionKey){
        optionReceiptRepository.deleteOptionReceiptByOptionKey(optionKey);
    }

    @Override
    public OptionReceipt dtoToEntity(OptionReceiptDTO dto){
        return OptionReceipt.builder()
                .id(new OptionReceiptID(dto.getOptionId(), dto.getStockId()))
                .quantity(dto.getQuantity())
                .option(dto.getOptionDTO()!=null? optionService.dtoToEntity(dto.getOptionDTO()):null)
                .stock(dto.getStockDTO()!=null? stockService.dtoToEntity(dto.getStockDTO()):null)
                .build();
    }
    @Override
    public OptionReceiptDTO entityToDto(OptionReceipt entity){
        return OptionReceiptDTO.builder()
                .optionId(entity.getId().getOptionKey())
                .stockId(entity.getId().getStockKey())
                .quantity(entity.getQuantity())
                .optionDTO(entity.getOption()!=null? optionService.entityToDto(entity.getOption()):null)
                .stockDTO(entity.getStock()!=null? stockService.entityToDto(entity.getStock()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public Long countOptionReceiptByStock(StockDTO stockDTO){
        Stock stock=stockService.dtoToEntity(stockDTO);
        return optionReceiptRepository.countOptionReceiptByStock(stock);
    }
    @Override
    public List<OptionQuantityDTO> getOptionListByStock(StockDTO stockDTO){
        Stock stock = stockService.dtoToEntity(stockDTO);
        return optionReceiptRepository.getOptionListByStock(stock);
    }
    @Override
    public List<OptionDTO> getOptionSoldOutList(StockDTO stockDTO, Boolean soldOut){
        Stock stock = stockService.dtoToEntity(stockDTO);
        List<Option> entityList = optionReceiptRepository.getOptionSoldOut(stock, soldOut);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(optionService::entityToDto).toList();
    }
    @Override
    public List<OptionReceiptDTO> getOptionReceiptByOption(OptionDTO optionDTO){
        Option option = optionService.dtoToEntity(optionDTO);
        List<OptionReceipt> entityList = optionReceiptRepository.getOptionReceiptByOption(option);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
}
