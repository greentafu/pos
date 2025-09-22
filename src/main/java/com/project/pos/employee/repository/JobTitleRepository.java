package com.project.pos.employee.repository;

import com.project.pos.employee.entity.JobTitle;
import com.project.pos.employee.entity.Wage;
import com.project.pos.employee.repositoryDSL.JobTitleRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobTitleRepository extends JpaRepository<JobTitle, Long>, JobTitleRepositoryCustom {
    @Query("select count(jt) from JobTitle jt " +
            "where jt.wage=:wage and jt.deleted=false")
    Long countJobTitleByWage(@Param("wage") Wage wage);
}
