package com.project.pos.home.repository;

import com.project.pos.home.entity.Login;
import com.project.pos.home.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    @Query("select o from Owner o where o.login=:login")
    Owner getOwnerDTOByLogin(@Param("login") Login login);
}
