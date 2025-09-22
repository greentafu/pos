package com.project.pos.revenue.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.revenue.dto.responseDTO.PointPageDTO;
import com.project.pos.revenue.entity.QPoint;
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

public class PointRepositoryImpl implements PointRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    PointRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<PointPageDTO> getPointPage(PageRequestDTO requestDTO){
        QPoint qPoint=QPoint.point;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qPoint.member.id.eq(requestDTO.getSelectedId()));

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<PointPageDTO> content = queryFactory
                .select(Projections.constructor(
                        PointPageDTO.class,
                        qPoint.id, qPoint.regDate, qPoint.changingPoint, qPoint.typeValue, qPoint.remainingPoint
                ))
                .from(qPoint)
                .where(builder)
                .groupBy(qPoint.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qPoint.id.asc())
                .fetch();

        Long total = queryFactory
                .select(qPoint.countDistinct())
                .from(qPoint)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
