package com.project.pos.employee.repository;

import com.project.pos.employee.entity.Employee;
import com.project.pos.employee.entity.JobTitle;
import com.project.pos.employee.repositoryDSL.EmployeeRepositoryCustom;
import com.project.pos.home.entity.Login;
import com.project.pos.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, EmployeeRepositoryCustom {
    @Query("select max(e.number) from Employee e " +
            "where e.jobTitle.store=:store")
    Long maxEmployeeNumber(@Param("store") Store store);

    @Query("select count(e) from Employee e " +
            "where e.jobTitle=:jobTitle and e.deleted=false")
    Long countEmployeeByJobTitle(@Param("jobTitle") JobTitle jobTitle);

    @Query("select e from Employee e where e.login=:login")
    Employee getEmployeeByLogin(@Param("login") Login login);
}
