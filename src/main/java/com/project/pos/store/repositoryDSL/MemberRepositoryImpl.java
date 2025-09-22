package com.project.pos.store.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.responseDTO.MemberPageDTO;
import com.project.pos.store.entity.QMember;
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

public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    MemberRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<MemberPageDTO> getMemberPage(PageRequestDTO requestDTO){
        QMember qMember=QMember.member;
        QStore qStore=QStore.store;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qMember.deleted.isFalse());
        builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qMember.phoneNumber.containsIgnoreCase(requestDTO.getSearchText()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<MemberPageDTO> content = queryFactory
                .select(Projections.constructor(
                        MemberPageDTO.class,
                        qMember.id, qMember.phoneNumber, qMember.modDate, qMember.points
                ))
                .from(qMember)
                .leftJoin(qMember.store, qStore)
                .where(builder)
                .groupBy(qMember.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qMember.id.asc())
                .fetch();

        Long total = queryFactory
                .select(qMember.countDistinct())
                .from(qMember)
                .leftJoin(qMember.store, qStore)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
