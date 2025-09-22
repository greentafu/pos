package com.project.pos.option.repositoryDSL;

import com.project.pos.dslDTO.SoldOutListRequestDTO;
import com.project.pos.option.dto.OptionCategoryDTO;
import com.project.pos.option.dto.OptionDTO;
import com.project.pos.option.dto.queryDTO.OptionSoldOutDTO;
import com.project.pos.option.dto.queryDTO.OrderOptionDTO;
import com.project.pos.option.dto.responseDTO.OptionPageDTO;
import com.project.pos.option.entity.*;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.stock.entity.Stock;
import com.project.pos.store.entity.Store;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OptionRepositoryImpl implements OptionRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    OptionRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<OptionPageDTO> getOptionPage(PageRequestDTO requestDTO){
        QOption qOption=QOption.option;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOption.deleted.isFalse());
        builder.and(qOption.store.id.eq(requestDTO.getStoreDTO().getId()));
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qOption.name.containsIgnoreCase(requestDTO.getSearchText()));
        }
        if (requestDTO.getSearchCategory() !=null && requestDTO.getSearchCategory()!=0) {
            builder.and(qOption.optionCategory.id.eq(requestDTO.getSearchCategory()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<OptionPageDTO> content = queryFactory
                .select(Projections.constructor(
                        OptionPageDTO.class,
                        qOption.id, qOption.number, qOption.name, qOption.optionCategory.name, qOption.optionPrice
                ))
                .from(qOption)
                .where(builder)
                .groupBy(qOption.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qOption.id.asc())
                .fetch();

        Long total = queryFactory
                .select(qOption.countDistinct())
                .from(qOption)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    @Override
    public List<Option> getSoldOutOptionList(SoldOutListRequestDTO requestDTO){
        QOption qOption=QOption.option;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOption.deleted.isFalse());
        builder.and(qOption.store.id.eq(requestDTO.getStoreDTO().getId()));
        builder.and(qOption.soldOut.eq(requestDTO.getSoldOutType()));
        if (requestDTO.getSearchCategory() !=null && requestDTO.getSearchCategory()!=0) {
            builder.and(qOption.optionCategory.id.eq(requestDTO.getSearchCategory()));
        }

        return queryFactory
                .select(qOption)
                .from(qOption)
                .where(builder)
                .orderBy(qOption.id.asc())
                .fetch();
    }

    @Override
    public List<OptionSoldOutDTO> getOptionSoldOutCountList(Stock stock){
        QOption qOption=QOption.option;
        QOptionReceipt qOptionReceipt=QOptionReceipt.optionReceipt;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOption.deleted.isFalse());
        builder.and(qOption.soldOutType.isTrue());

        return queryFactory
                .select(Projections.constructor(
                        OptionSoldOutDTO.class,
                        qOption.id,
                        qOptionReceipt.stock.soldOut
                                        .when(true).then(1L)
                                        .otherwise(0L)
                                        .sum()
                                        .coalesce(0L)
                ))
                .from(qOption)
                .leftJoin(qOptionReceipt).on(qOptionReceipt.option.eq(qOption))
                .where(builder)
                .groupBy(qOption.id)
                .orderBy(qOption.id.asc())
                .fetch();
    }

    @Override
    public List<OrderOptionDTO> getOrderOptionList(Store store){
        QOption qOption=QOption.option;
        QOptionCategory qOptionCategory=QOptionCategory.optionCategory;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOptionCategory.deleted.isFalse()).and(qOptionCategory.arrangement.isNotNull());
        builder.and(qOption.deleted.isFalse()).and(qOption.arrangement.isNotNull());

        List<Tuple> result = queryFactory
                .select(qOptionCategory, qOption)
                .from(qOption)
                .join(qOption.optionCategory, qOptionCategory)
                .where(builder)
                .orderBy(qOptionCategory.arrangement.asc(), qOption.arrangement.asc())
                .fetch();

        Map<Long, OrderOptionDTO> grouped = result.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(qOptionCategory).getId(),
                        tuple -> {
                            OptionCategory optionCategory = tuple.get(qOptionCategory);
                            Option option = tuple.get(qOption);

                            OptionDTO optionDTO = new OptionDTO(option.getId(), option.getNumber(), option.getName(), option.getDisplayName(),
                                    option.getOptionPrice(), option.getOptionCost(), option.getArrangement(),
                                    option.getSoldOutType(), option.getSoldOut());

                            List<OptionDTO> optioinList = new ArrayList<>();
                            optioinList.add(optionDTO);
                            return new OrderOptionDTO(optionCategory.getId(), optionCategory.getName(),
                                    optionCategory.getArrangement(), optionCategory.getMulti(), optioinList);
                        },
                        (existing, incoming) -> {
                            existing.getOptionList().addAll(incoming.getOptionList());
                            return existing;
                        }
                ));

        return grouped.values().stream().toList();
    }
}
