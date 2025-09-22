package com.project.pos.store.repository;

import com.project.pos.home.entity.Owner;
import com.project.pos.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query("select count(s) from Store s where s.owner=:owner and s.deleted=false")
    Long getStoreCountByOwner(@Param("owner") Owner owner);
}
