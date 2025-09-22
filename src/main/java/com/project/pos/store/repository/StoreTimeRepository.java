package com.project.pos.store.repository;

import com.project.pos.store.entity.Store;
import com.project.pos.store.entity.StoreTime;
import com.project.pos.store.entity.StoreTimeID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreTimeRepository extends JpaRepository<StoreTime, StoreTimeID> {
    @Query("select st from StoreTime st where st.store=:store order by st.id asc")
    List<StoreTime> storeTimeListByStore(@Param("store") Store store);
}
