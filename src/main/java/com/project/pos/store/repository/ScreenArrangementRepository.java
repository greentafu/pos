package com.project.pos.store.repository;

import com.project.pos.store.entity.ScreenArrangement;
import com.project.pos.store.entity.ScreenArrangementID;
import com.project.pos.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScreenArrangementRepository extends JpaRepository<ScreenArrangement, ScreenArrangementID> {
    @Query("select sa from ScreenArrangement sa " +
            "where sa.store=:store and sa.page=1 " +
            "order by sa.indexValue asc")
    List<ScreenArrangement> page1List(@Param("store") Store store);

    @Query("select sa from ScreenArrangement sa " +
            "where sa.store=:store and sa.page=2 " +
            "order by sa.indexValue asc")
    List<ScreenArrangement> page2List(@Param("store") Store store);
}
