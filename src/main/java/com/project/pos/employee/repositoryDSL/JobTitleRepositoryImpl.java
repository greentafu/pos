package com.project.pos.employee.repositoryDSL;

import com.project.pos.employee.dto.responseDTO.JobTitlePageDTO;
import com.project.pos.employee.entity.QEmployee;
import com.project.pos.employee.entity.QJobTitle;
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

public class JobTitleRepositoryImpl implements JobTitleRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired
    JobTitleRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<JobTitlePageDTO> getJobTitlePage(PageRequestDTO requestDTO){
        QJobTitle qJobTitle=QJobTitle.jobTitle;
        QStore qStore=QStore.store;
        QEmployee qEmployee=QEmployee.employee;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qJobTitle.deleted.isFalse());
        builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qJobTitle.name.containsIgnoreCase(requestDTO.getSearchText()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<JobTitlePageDTO> content = queryFactory
                .select(Projections.constructor(
                        JobTitlePageDTO.class,
                        qJobTitle.id, qJobTitle.name, qJobTitle.wage.perWage,
                        qEmployee.deleted
                                .when(false).then(1L)
                                .otherwise(0L)
                                .sum()
                                .coalesce(0L)
                ))
                .from(qJobTitle)
                .leftJoin(qJobTitle.store, qStore)
                .leftJoin(qEmployee).on(qEmployee.jobTitle.eq(qJobTitle))
                .where(builder)
                .groupBy(qJobTitle.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qJobTitle.id.asc())
                .fetch();

        Long total = queryFactory
                .select(qJobTitle.countDistinct())
                .from(qJobTitle)
                .leftJoin(qJobTitle.store, qStore)
                .leftJoin(qEmployee).on(qEmployee.jobTitle.eq(qJobTitle))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
