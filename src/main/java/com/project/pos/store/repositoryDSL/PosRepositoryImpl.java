package com.project.pos.store.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.responseDTO.PosPageDTO;
import com.project.pos.store.entity.QPos;
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

public class PosRepositoryImpl implements PosRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    PosRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<PosPageDTO> getPosPage(PageRequestDTO requestDTO){
        QPos qPos=QPos.pos;
        QStore qStore=QStore.store;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));
        builder.and(qPos.deleted.isFalse());
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qPos.name.containsIgnoreCase(requestDTO.getSearchText()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<PosPageDTO> content = queryFactory
                .select(Projections.constructor(
                        PosPageDTO.class,
                        qPos.id, qPos.number, qPos.name, qPos.location, qPos.status
                ))
                .from(qPos)
                .leftJoin(qPos.store, qStore)
                .where(builder)
                .groupBy(qPos.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPos.id.asc())
                .fetch();

        Long total = queryFactory
                .select(qPos.countDistinct())
                .from(qPos)
                .leftJoin(qPos.store, qStore)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
