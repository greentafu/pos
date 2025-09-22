package com.project.pos.store.repository;

import com.project.pos.store.entity.Pos;
import com.project.pos.store.entity.Store;
import com.project.pos.store.repositoryDSL.PosRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PosRepository extends JpaRepository<Pos, Long>, PosRepositoryCustom {
    @Query("select p.id from Pos p where p.store=:store and p.number=:number and p.deleted=false")
    Long countPosByNumber(@Param("store") Store store, @Param("number") Long number);
    @Query("select p.id from Pos p where p.store=:store and p.machineId=:machineId and p.deleted=false")
    Long countPosByMachineID(@Param("store") Store store, @Param("machineId") String machineId);
    @Query("select p from Pos p where p.store=:store and p.deleted=false")
    List<Pos> getPosListByStore(@Param("store") Store store);
}
