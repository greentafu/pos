package com.project.pos.employee.repositoryDSL;

import com.project.pos.employee.dto.responseDTO.WagePageDTO;
import com.project.pos.employee.entity.QEmployee;
import com.project.pos.employee.entity.QWage;
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

public class WageRepositoryImpl implements WageRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired
    WageRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<WagePageDTO> getWagePage(PageRequestDTO requestDTO){
        QWage qWage=QWage.wage;
        QStore qStore=QStore.store;
        QEmployee qEmployee=QEmployee.employee;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qWage.deleted.isFalse());
        builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qWage.name.containsIgnoreCase(requestDTO.getSearchText()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<WagePageDTO> content = queryFactory
                .select(Projections.constructor(
                        WagePageDTO.class,
                        qWage.id, qWage.name, qWage.perWage, qEmployee.count()
                ))
                .from(qWage)
                .leftJoin(qWage.store, qStore)
                .leftJoin(qEmployee).on(qEmployee.jobTitle.wage.eq(qWage).and(qEmployee.deleted.isFalse()))
                .where(builder)
                .groupBy(qWage.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qWage.id.asc())
                .fetch();

        Long total = queryFactory
                .select(qWage.countDistinct())
                .from(qWage)
                .leftJoin(qWage.store, qStore)
                .leftJoin(qEmployee).on(qEmployee.jobTitle.wage.eq(qWage).and(qEmployee.deleted.isFalse()))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
