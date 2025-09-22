package com.project.pos.employee.repository;

import com.project.pos.employee.entity.Commute;
import com.project.pos.employee.entity.Employee;
import com.project.pos.employee.repositoryDSL.CommuteRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CommuteRepository extends JpaRepository<Commute, Long>, CommuteRepositoryCustom {
    @Query("select c from Commute c " +
            "where c.employee=:employee and c.startTime>=:start and c.endTime<:end " +
            "order by c.id desc")
    List<Commute> getCommuteList(@Param("employee") Employee employee,
                                 @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("select c from Commute c " +
            "where c.employee=:employee " +
            "and ((c.startTime>=:start and c.startTime<:end) or (c.startTime is not null and c.endTime is null)) " +
            "order by c.id desc")
    List<Commute> getCommuteListByEmployee(@Param("employee") Employee employee,
                                           @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
