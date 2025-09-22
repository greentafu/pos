package com.project.pos.employee.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.employee.dto.queryDTO.WorkTimeDTO;
import com.project.pos.employee.dto.responseDTO.CommutePageDTO;
import com.project.pos.employee.dto.responseDTO.PaymentPageDTO;
import com.project.pos.employee.dto.responseDTO.PaymentSimplePageDTO;
import com.project.pos.employee.entity.*;
import com.project.pos.store.entity.QStore;
import com.project.pos.store.entity.Store;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
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
import java.util.Map;
import java.util.stream.Collectors;

public class CommuteRepositoryImpl implements CommuteRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Autowired
    CommuteRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Employee> getWorkingEmployee(Store store){
        QEmployee qEmployee=QEmployee.employee;
        QStore qStore=QStore.store;
        QCommute qCommute=QCommute.commute;
        QCommute qCommuteSub=new QCommute("qCommuteSub");

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qEmployee.deleted.isFalse());
        builder.and(qEmployee.jobTitle.store.id.eq(store.getId()));

        return queryFactory
                .select(qEmployee).distinct()
                .from(qEmployee)
                .leftJoin(qEmployee.jobTitle.store, qStore)
                .innerJoin(qCommute).on(qCommute.id.eq(
                        JPAExpressions
                                .select(qCommuteSub.id.max())
                                .from(qCommuteSub)
                                .where(qCommuteSub.employee.id.eq(qEmployee.id)
                                        .and(qCommuteSub.startTime.isNotNull())
                                        .and(qCommuteSub.endTime.isNull()))
                ))
                .where(builder)
                .fetch();
    }
    @Override
    public Page<CommutePageDTO> getCommuteDetailPage(PageRequestDTO requestDTO){
        QEmployee qEmployee=QEmployee.employee;
        QCommute qCommute=QCommute.commute;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qCommute.employee.id.eq(requestDTO.getSelectedId()));
        builder.and(qCommute.startTime.between(requestDTO.getStartDate(), requestDTO.getEndDate()));

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
                        qCommute.id, qEmployee.number, qEmployee.name, qEmployee.jobTitle.name,
                        startDate, endDate, qCommute.notes
                ))
                .from(qCommute)
                .leftJoin(qCommute.employee, qEmployee)
                .where(builder)
                .orderBy(qCommute.startTime.desc(), qCommute.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(qCommute.countDistinct())
                .from(qCommute)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
    @Override
    public Page<PaymentPageDTO> getPaymentPage(PageRequestDTO requestDTO){
        QCommute qCommute=QCommute.commute;
        QStore qStore=QStore.store;
        QEmployee qEmployee=QEmployee.employee;
        QJobTitle qJobTitle=QJobTitle.jobTitle;
        QWage qWage=QWage.wage;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));
        builder.and(qCommute.endTime.between(requestDTO.getStartDate(), requestDTO.getEndDate()));
        builder.and(qCommute.modified.isFalse());
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qEmployee.name.containsIgnoreCase(requestDTO.getSearchText()));
        }
        if (requestDTO.getSearchCategory() != null && requestDTO.getSearchCategory() != 0L) {
            builder.and(qWage.id.eq(requestDTO.getSearchCategory()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<PaymentPageDTO> content = queryFactory
                .select(Projections.constructor(
                        PaymentPageDTO.class,
                        qCommute.id, qEmployee.number, qEmployee.name, qWage.name, qWage.perWage,
                        qCommute.startTime, qCommute.endTime
                ))
                .from(qCommute)
                .leftJoin(qCommute.store, qStore)
                .leftJoin(qCommute.employee, qEmployee)
                .leftJoin(qEmployee.jobTitle, qJobTitle)
                .leftJoin(qJobTitle.wage, qWage)
                .where(builder)
                .orderBy(qCommute.startTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(qCommute.countDistinct())
                .from(qCommute)
                .leftJoin(qCommute.store, qStore)
                .leftJoin(qCommute.employee, qEmployee)
                .leftJoin(qEmployee.jobTitle, qJobTitle)
                .leftJoin(qJobTitle.wage, qWage)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
    @Override
    public Page<PaymentSimplePageDTO> getPaymentSimplePage(PageRequestDTO requestDTO){
        QCommute qCommute=QCommute.commute;
        QStore qStore=QStore.store;
        QEmployee qEmployee=QEmployee.employee;
        QJobTitle qJobTitle=QJobTitle.jobTitle;
        QWage qWage=QWage.wage;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));
        builder.and(qEmployee.deleted.isFalse());
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qEmployee.name.containsIgnoreCase(requestDTO.getSearchText()));
        }
        if (requestDTO.getSearchCategory() != null && requestDTO.getSearchCategory() != 0L) {
            builder.and(qWage.id.eq(requestDTO.getSearchCategory()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<Employee> employeeList = queryFactory
                .select(qEmployee)
                .from(qEmployee)
                .leftJoin(qEmployee.jobTitle, qJobTitle)
                .leftJoin(qJobTitle.store, qStore)
                .leftJoin(qJobTitle.wage, qWage)
                .where(builder)
                .orderBy(qEmployee.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long total = queryFactory
                .select(qEmployee.countDistinct())
                .from(qEmployee)
                .leftJoin(qEmployee.jobTitle, qJobTitle)
                .leftJoin(qJobTitle.store, qStore)
                .leftJoin(qJobTitle.wage, qWage)
                .where(builder)
                .fetchOne();

        List<Long> employeeIdList = employeeList.stream().map(Employee::getId).toList();
        BooleanBuilder subBuilder=new BooleanBuilder();
        subBuilder.and(qCommute.modified.isFalse());
        subBuilder.and(qCommute.employee.id.in(employeeIdList));
        subBuilder.and(qCommute.endTime.between(requestDTO.getStartDate(), requestDTO.getEndDate()));

        StringTemplate endDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                qCommute.endTime, Expressions.constant("%m/%d")
        );
        NumberExpression<Integer> minutesDiff = Expressions.numberTemplate(
                Integer.class,
                "FLOOR(TIMESTAMPDIFF(SECOND, {0}, {1}) / 60)",
                qCommute.startTime,
                qCommute.endTime
        );

        NumberExpression<Double> wagePerMinute =
                Expressions.numberTemplate(Double.class, "{0} / 60.0", qWage.perWage);

        NumberExpression<Integer> floored =
                Expressions.numberTemplate(
                        Integer.class,
                        "FLOOR({0} * {1})",
                        minutesDiff,
                        wagePerMinute
                );


        List<WorkTimeDTO> timeList = queryFactory
                .select(Projections.constructor(
                        WorkTimeDTO.class,
                        qEmployee.id, endDate, minutesDiff.sum(), floored.sum()
                ))
                .from(qCommute)
                .leftJoin(qCommute.employee, qEmployee)
                .leftJoin(qEmployee.jobTitle, qJobTitle)
                .leftJoin(qJobTitle.wage, qWage)
                .where(subBuilder)
                .groupBy(qEmployee.id, endDate)
                .orderBy(endDate.asc())
                .fetch();

        Map<Long, List<WorkTimeDTO>> tempMap = timeList.stream().collect(Collectors.groupingBy(WorkTimeDTO::getId));

        List<PaymentSimplePageDTO> result = employeeList.stream()
                .map(temp ->
                        new PaymentSimplePageDTO(temp.getId(), temp.getNumber(), temp.getName(),
                                tempMap.getOrDefault(temp.getId(), List.of()).stream()
                                        .collect(Collectors.toMap(WorkTimeDTO::getDate, b->b))
                        )
                )
                .toList();

        return new PageImpl<>(result, pageable, total != null ? total : 0);
    }
//@Override
//public Page<PaymentSimplePageDTO> getPaymentSimplePage(PageRequestDTO requestDTO){
//    QCommute qCommute=QCommute.commute;
//    QStore qStore=QStore.store;
//    QEmployee qEmployee=QEmployee.employee;
//    QJobTitle qJobTitle=QJobTitle.jobTitle;
//    QWage qWage=QWage.wage;
//
//    BooleanBuilder builder=new BooleanBuilder();
//    builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));
//    builder.and(qEmployee.deleted.isFalse());
//    if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
//        builder.and(qEmployee.name.containsIgnoreCase(requestDTO.getSearchText()));
//    }
//    if (requestDTO.getSearchCategory() != null && requestDTO.getSearchCategory() != 0L) {
//        builder.and(qWage.id.eq(requestDTO.getSearchCategory()));
//    }
//
//    Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
//    List<Employee> employeeList = queryFactory
//            .select(qEmployee)
//            .from(qEmployee)
//            .leftJoin(qEmployee.jobTitle, qJobTitle)
//            .leftJoin(qJobTitle.store, qStore)
//            .leftJoin(qJobTitle.wage, qWage)
//            .where(builder)
//            .orderBy(qEmployee.id.desc())
//            .offset(pageable.getOffset())
//            .limit(pageable.getPageSize())
//            .fetch();
//    Long total = queryFactory
//            .select(qEmployee.countDistinct())
//            .from(qEmployee)
//            .leftJoin(qEmployee.jobTitle, qJobTitle)
//            .leftJoin(qJobTitle.store, qStore)
//            .leftJoin(qJobTitle.wage, qWage)
//            .where(builder)
//            .fetchOne();
//
//    List<Long> employeeIdList = employeeList.stream().map(Employee::getId).toList();
//    BooleanBuilder subBuilder=new BooleanBuilder();
//    subBuilder.and(qCommute.modified.isFalse());
//    subBuilder.and(qCommute.employee.id.in(employeeIdList));
//    subBuilder.and(qCommute.endTime.between(requestDTO.getStartDate(), requestDTO.getEndDate()));
//
//    StringTemplate endDate = Expressions.stringTemplate(
//            "DATE_FORMAT({0}, {1})",
//            qCommute.endTime, Expressions.constant("%m/%d")
//    );
//    NumberExpression<Integer> minutesDiff = Expressions.numberTemplate(
//            Integer.class,
//            "FLOOR(TIMESTAMPDIFF(SECOND, {0}, {1}) / 60)",
//            qCommute.startTime,
//            qCommute.endTime
//    );
//    List<WorkTimeDTO> timeList = queryFactory
//            .select(Projections.constructor(
//                    WorkTimeDTO.class,
//                    qEmployee.id, endDate, minutesDiff.sum(), minutesDiff.multiply(qWage.perWage.divide(60)).sum()
//            ))
//            .from(qCommute)
//            .leftJoin(qCommute.employee, qEmployee)
//            .leftJoin(qEmployee.jobTitle, qJobTitle)
//            .leftJoin(qJobTitle.wage, qWage)
//            .where(subBuilder)
//            .groupBy(qEmployee.id, endDate)
//            .orderBy(endDate.asc())
//            .fetch();
//
//    Map<Long, List<WorkTimeDTO>> tempMap = timeList.stream().collect(Collectors.groupingBy(WorkTimeDTO::getId));
//
//    List<PaymentSimplePageDTO> result = employeeList.stream()
//            .map(temp ->
//                    new PaymentSimplePageDTO(temp.getId(), temp.getNumber(), temp.getName(),
//                            tempMap.getOrDefault(temp.getId(), List.of()).stream()
//                                    .collect(Collectors.toMap(WorkTimeDTO::getDate, b->b))
//                    )
//            )
//            .toList();
//
//    return new PageImpl<>(result, pageable, total != null ? total : 0);
//}
}
