package com.project.pos.employee.repository;

import com.project.pos.employee.entity.Wage;
import com.project.pos.employee.repositoryDSL.WageRepositoryCustom;
import com.project.pos.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WageRepository extends JpaRepository<Wage, Long>, WageRepositoryCustom {
    @Query("select w from Wage w " +
            "where w.store=:store and w.deleted=false " +
            "order by w.id desc")
    List<Wage> getWageListByStore(@Param("store") Store store);
}
