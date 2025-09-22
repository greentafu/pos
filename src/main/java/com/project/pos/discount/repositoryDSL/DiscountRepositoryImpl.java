package com.project.pos.discount.repositoryDSL;

import com.project.pos.discount.dto.DiscountDTO;
import com.project.pos.discount.dto.queryDTO.OrderDiscountDTO;
import com.project.pos.discount.entity.Discount;
import com.project.pos.discount.entity.DiscountCategory;
import com.project.pos.discount.entity.QDiscountCategory;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.discount.dto.responseDTO.DiscountPageDTO;
import com.project.pos.discount.entity.QDiscount;
import com.project.pos.store.entity.QStore;
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

public class DiscountRepositoryImpl implements DiscountRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    DiscountRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<DiscountPageDTO> getDiscountPage(PageRequestDTO requestDTO){
        QDiscount qDiscount=QDiscount.discount;
        QStore qStore=QStore.store;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qDiscount.deleted.isFalse());
        builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qDiscount.name.containsIgnoreCase(requestDTO.getSearchText()));
        }
        if (requestDTO.getSearchCategory() !=null && requestDTO.getSearchCategory()!=0) {
            builder.and(qDiscount.discountCategory.id.eq(requestDTO.getSearchCategory()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<DiscountPageDTO> content = queryFactory
                .select(Projections.constructor(
                        DiscountPageDTO.class,
                        qDiscount.id, qDiscount.number, qDiscount.name, qDiscount.discountCategory.name,
                        qDiscount.discountPrice, qDiscount.typeValue
                ))
                .from(qDiscount)
                .leftJoin(qDiscount.store, qStore)
                .where(builder)
                .groupBy(qDiscount.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qDiscount.id.asc())
                .fetch();

        Long total = queryFactory
                .select(qDiscount.countDistinct())
                .from(qDiscount)
                .leftJoin(qDiscount.store, qStore)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    @Override
    public List<OrderDiscountDTO> getOrderDiscountList(Store store){
        QDiscount qDiscount=QDiscount.discount;
        QDiscountCategory qDiscountCategory=QDiscountCategory.discountCategory;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qDiscountCategory.deleted.isFalse()).and(qDiscountCategory.arrangement.isNotNull());
        builder.and(qDiscount.deleted.isFalse()).and(qDiscount.arrangement.isNotNull());

        List<Tuple> result = queryFactory
                .select(qDiscountCategory, qDiscount)
                .from(qDiscount)
                .join(qDiscount.discountCategory, qDiscountCategory)
                .where(builder)
                .orderBy(qDiscountCategory.arrangement.asc(), qDiscount.arrangement.asc())
                .fetch();

        Map<Long, OrderDiscountDTO> grouped = result.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(qDiscountCategory).getId(),
                        tuple -> {
                            DiscountCategory discountCategory = tuple.get(qDiscountCategory);
                            Discount discount = tuple.get(qDiscount);

                            DiscountDTO discountDTO = new DiscountDTO(discount.getId(), discount.getNumber()
                                    , discount.getName(), discount.getDisplayName(), discount.getTypeValue(),
                                    discount.getDiscountPrice(), discount.getArrangement());

                            List<DiscountDTO> discountList = new ArrayList<>();
                            discountList.add(discountDTO);
                            return new OrderDiscountDTO(discountCategory.getId(), discountCategory.getName(),
                                    discountCategory.getArrangement(), discountCategory.getMulti(), discountList);
                        },
                        (existing, incoming) -> {
                            existing.getDiscountList().addAll(incoming.getDiscountList());
                            return existing;
                        }
                ));

        return grouped.values().stream().toList();
    }
}
