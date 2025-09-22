package com.project.pos.home.repository;

import com.project.pos.home.entity.Login;
import com.project.pos.home.entity.LoginStore;
import com.project.pos.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoginStoreRepository extends JpaRepository<LoginStore, Long> {
    @Query("select ls.store from LoginStore ls " +
            "where ls.login=:login and ls.store.deleted=false")
    List<Store> getStoreListByLogin(@Param("login") Login login);
}
