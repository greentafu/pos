package com.project.pos.product.repositoryDSL;

import com.project.pos.product.dto.responseDTO.ProductCategoryPageDTO;
import com.project.pos.product.entity.ProductCategory;
import com.project.pos.product.entity.QProduct;
import com.project.pos.product.entity.QProductCategory;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.entity.QStore;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ProductCategoryRepositoryImpl implements ProductCategoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    ProductCategoryRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<ProductCategoryPageDTO> getProductCategoryPage(PageRequestDTO requestDTO){
        QProductCategory qProductCategory=QProductCategory.productCategory;
        QStore qStore=QStore.store;
        QProduct qProduct=QProduct.product;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qProductCategory.deleted.isFalse());
        builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qProductCategory.name.containsIgnoreCase(requestDTO.getSearchText()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<ProductCategoryPageDTO> content = queryFactory
                .select(Projections.constructor(
                        ProductCategoryPageDTO.class,
                        qProductCategory.id, qProductCategory.name,
                        qProduct.deleted
                                .when(false).then(1L)
                                .otherwise(0L)
                                .sum()
                                .coalesce(0L)
                ))
                .from(qProductCategory)
                .leftJoin(qProductCategory.store, qStore)
                .leftJoin(qProduct).on(qProduct.productCategory.eq(qProductCategory))
                .where(builder)
                .groupBy(qProductCategory.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProductCategory.id.asc())
                .fetch();

        Long total = queryFactory
                .select(qProductCategory.countDistinct())
                .from(qProductCategory)
                .leftJoin(qProductCategory.store, qStore)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    @Override
    public Page<ProductCategory> getMenuCategoryPage(PageRequestDTO requestDTO){
        QProductCategory qProductCategory=QProductCategory.productCategory;
        QStore qStore=QStore.store;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qProductCategory.deleted.isFalse());
        builder.and(qProductCategory.arrangement.isNotNull());
        builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<ProductCategory> content = queryFactory
                .select(qProductCategory)
                .from(qProductCategory)
                .leftJoin(qProductCategory.store, qStore)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProductCategory.arrangement.asc())
                .fetch();

        Long total = queryFactory
                .select(qProductCategory.countDistinct())
                .from(qProductCategory)
                .leftJoin(qProductCategory.store, qStore)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
