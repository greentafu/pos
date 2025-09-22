package com.project.pos.home.repository;

import com.project.pos.home.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoginRepository extends JpaRepository<Login, Long> {
    @Query("select l from Login l where l.userId = :id and l.deleted = false")
    Login findByUserId(@Param("id") String id);
}
