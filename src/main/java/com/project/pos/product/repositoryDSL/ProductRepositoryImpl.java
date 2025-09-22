package com.project.pos.product.repositoryDSL;

import com.project.pos.dslDTO.SoldOutListRequestDTO;
import com.project.pos.product.dto.queryDTO.ProductSoldOutDTO;
import com.project.pos.product.entity.Product;
import com.project.pos.product.entity.QProduct;
import com.project.pos.product.dto.responseDTO.ProductPageDTO;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.product.entity.QProductReceipt;
import com.project.pos.stock.entity.Stock;
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

public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    ProductRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<ProductPageDTO> getProductPage(PageRequestDTO requestDTO){
        QProduct qProduct=QProduct.product;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qProduct.deleted.isFalse());
        builder.and(qProduct.store.id.eq(requestDTO.getStoreDTO().getId()));
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qProduct.name.containsIgnoreCase(requestDTO.getSearchText()));
        }
        if (requestDTO.getSearchCategory() !=null && requestDTO.getSearchCategory()!=0) {
            builder.and(qProduct.productCategory.id.eq(requestDTO.getSearchCategory()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<ProductPageDTO> content = queryFactory
                .select(Projections.constructor(
                        ProductPageDTO.class,
                        qProduct.id, qProduct.number, qProduct.name, qProduct.productCategory.name, qProduct.productPrice
                ))
                .from(qProduct)
                .where(builder)
                .groupBy(qProduct.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qProduct.id.asc())
                .fetch();

        Long total = queryFactory
                .select(qProduct.countDistinct())
                .from(qProduct)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    @Override
    public List<Product> getSoldOutProductList(SoldOutListRequestDTO requestDTO){
        QProduct qProduct=QProduct.product;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qProduct.deleted.isFalse());
        builder.and(qProduct.store.id.eq(requestDTO.getStoreDTO().getId()));
        builder.and(qProduct.soldOut.eq(requestDTO.getSoldOutType()));
        if (requestDTO.getSearchCategory() !=null && requestDTO.getSearchCategory()!=0) {
            builder.and(qProduct.productCategory.id.eq(requestDTO.getSearchCategory()));
        }

        return queryFactory
                .select(qProduct)
                .from(qProduct)
                .where(builder)
                .orderBy(qProduct.id.asc())
                .fetch();
    }

    @Override
    public List<ProductSoldOutDTO> getProductSoldOutCountList(Stock stock){
        QProduct qProduct=QProduct.product;
        QProductReceipt qProductReceipt = QProductReceipt.productReceipt;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qProduct.deleted.isFalse());
        builder.and(qProduct.soldOutType.isTrue());

        return queryFactory
                .select(Projections.constructor(
                        ProductSoldOutDTO.class,
                        qProduct.id,
                        qProductReceipt.stock.soldOut
                                .when(true).then(1L)
                                .otherwise(0L)
                                .sum()
                                .coalesce(0L)
                ))
                .from(qProduct)
                .leftJoin(qProductReceipt).on(qProductReceipt.product.eq(qProduct))
                .where(builder)
                .groupBy(qProduct.id)
                .orderBy(qProduct.id.asc())
                .fetch();
    }
}
