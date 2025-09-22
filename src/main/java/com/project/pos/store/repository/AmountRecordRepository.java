package com.project.pos.store.repository;

import com.project.pos.store.entity.AmountRecord;
import com.project.pos.store.entity.Pos;
import com.project.pos.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AmountRecordRepository extends JpaRepository<AmountRecord, Long> {
    @Query("select a from AmountRecord a " +
            "where a.store=:store and a.pos=:pos and a.finished is null")
    AmountRecord getExistAmountRecord(@Param("store") Store store, @Param("pos") Pos pos);
}
