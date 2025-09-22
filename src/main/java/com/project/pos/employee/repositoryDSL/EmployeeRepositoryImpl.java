package com.project.pos.employee.repositoryDSL;

import com.project.pos.employee.dto.responseDTO.CommutePageDTO;
import com.project.pos.employee.dto.responseDTO.EmployeePageDTO;
import com.project.pos.employee.entity.QCommute;
import com.project.pos.employee.entity.QEmployee;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.entity.QStore;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired
    EmployeeRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<EmployeePageDTO> getEmployeePage(PageRequestDTO requestDTO){
        QEmployee qEmployee=QEmployee.employee;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qEmployee.deleted.isFalse());
        builder.and(qEmployee.jobTitle.store.id.eq(requestDTO.getStoreDTO().getId()));
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qEmployee.name.containsIgnoreCase(requestDTO.getSearchText()));
        }
        if (requestDTO.getSearchCategory() !=null && requestDTO.getSearchCategory()!=0) {
            builder.and(qEmployee.jobTitle.id.eq(requestDTO.getSearchCategory()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<EmployeePageDTO> content = queryFactory
                .select(Projections.constructor(
                        EmployeePageDTO.class,
                        qEmployee.id, qEmployee.number, qEmployee.name, qEmployee.jobTitle.name, qEmployee.regDate, qEmployee.telNumber
                ))
                .from(qEmployee)
                .where(builder)
                .groupBy(qEmployee.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qEmployee.id.asc())
                .fetch();

        Long total = queryFactory
                .select(qEmployee.countDistinct())
                .from(qEmployee)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
    @Override
    public Page<CommutePageDTO> getCommutePage(PageRequestDTO requestDTO){
        QEmployee qEmployee=QEmployee.employee;
        QStore qStore=QStore.store;
        QCommute qCommute=QCommute.commute;
        QCommute qCommuteSub=new QCommute("qCommuteSub");

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qEmployee.deleted.isFalse());
        builder.and(qEmployee.jobTitle.store.id.eq(requestDTO.getStoreDTO().getId()));
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qEmployee.name.containsIgnoreCase(requestDTO.getSearchText()));
        }

        StringTemplate startDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                qCommute.startTime, Expressions.constant("%Y-%m-%d %H:%i")
        );
        StringTemplate endDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                qCommute.endTime, Expressions.constant("%Y-%m-%d %H:%i")
        );

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<CommutePageDTO> content = queryFactory
                .select(Projections.constructor(
                        CommutePageDTO.class,
                        qEmployee.id, qEmployee.number, qEmployee.name, qEmployee.jobTitle.name,
                        startDate, endDate, qCommute.notes
                ))
                .from(qEmployee)
                .leftJoin(qEmployee.jobTitle.store, qStore)
                .leftJoin(qCommute).on(qCommute.startTime.eq(
                        JPAExpressions
                                .select(qCommuteSub.startTime.max())
                                .from(qCommuteSub)
                                .where(qCommuteSub.employee.id.eq(qEmployee.id)
                                        .and(qCommute.startTime.between(requestDTO.getStartDate(), requestDTO.getEndDate())
                                                .or(qCommute.startTime.isNotNull().and(qCommute.endTime.isNull()))))
                ))
                .where(builder)
                .groupBy(qEmployee.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(qEmployee.countDistinct())
                .from(qEmployee)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
//    @Override
//    public Page<CommutePageDTO> getCommutePage(PageRequestDTO requestDTO){
//        QEmployee qEmployee=QEmployee.employee;
//        QStore qStore=QStore.store;
//        QCommute qCommute=QCommute.commute;
//        QCommute qCommuteSub=new QCommute("qCommuteSub");
//
//        BooleanBuilder builder=new BooleanBuilder();
//        builder.and(qEmployee.deleted.isFalse());
//        builder.and(qEmployee.jobTitle.store.id.eq(requestDTO.getStoreDTO().getId()));
//        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
//            builder.and(qEmployee.name.containsIgnoreCase(requestDTO.getSearchText()));
//        }
//
//        StringTemplate startDate = Expressions.stringTemplate(
//                "DATE_FORMAT({0}, {1})",
//                qCommute.startTime, Expressions.constant("%Y-%m-%d %H:%i")
//        );
//        StringTemplate endDate = Expressions.stringTemplate(
//                "DATE_FORMAT({0}, {1})",
//                qCommute.endTime, Expressions.constant("%Y-%m-%d %H:%i")
//        );
//
//        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
//        List<CommutePageDTO> content = queryFactory
//                .select(Projections.constructor(
//                        CommutePageDTO.class,
//                        qEmployee.id, qEmployee.number, qEmployee.name, qEmployee.jobTitle.name,
//                        startDate, endDate, qCommute.notes
//                ))
//                .from(qEmployee)
//                .leftJoin(qEmployee.jobTitle.store, qStore)
//                .leftJoin(qCommute).on(qCommute.id.eq(
//                        JPAExpressions
//                                .select(qCommuteSub.id.max())
//                                .from(qCommuteSub)
//                                .where(qCommuteSub.employee.id.eq(qEmployee.id)
//                                        .and(qCommute.startTime.between(requestDTO.getStartDate(), requestDTO.getEndDate())
//                                                .or(qCommute.startTime.isNotNull().and(qCommute.endTime.isNull()))))
//                ))
//                .where(builder)
//                .groupBy(qEmployee.id)
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        Long total = queryFactory
//                .select(qEmployee.countDistinct())
//                .from(qEmployee)
//                .where(builder)
//                .fetchOne();
//
//        return new PageImpl<>(content, pageable, total != null ? total : 0);
//    }
}
