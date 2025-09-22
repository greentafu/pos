package com.project.pos.store.repositoryDSL;

import com.project.pos.employee.entity.QEmployee;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.dto.responseDTO.MailPageDTO;
import com.project.pos.store.entity.QMail;
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

public class MailRepositoryImpl implements MailRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    MailRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<MailPageDTO> getMailPage(PageRequestDTO requestDTO){
        QMail qMail=QMail.mail;
        QStore qStore=QStore.store;
        QEmployee qEmployee=QEmployee.employee;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qMail.title.containsIgnoreCase(requestDTO.getSearchText()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<MailPageDTO> content = queryFactory
                .select(Projections.constructor(
                        MailPageDTO.class,
                        qMail.id, qMail.title, qMail.modDate, qMail.notes
                ))
                .from(qMail)
                .leftJoin(qMail.store, qStore)
                .where(builder)
                .groupBy(qMail.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qMail.id.asc())
                .fetch();

        Long total = queryFactory
                .select(qMail.countDistinct())
                .from(qMail)
                .leftJoin(qMail.store, qStore)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
