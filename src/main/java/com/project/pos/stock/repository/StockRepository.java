package com.project.pos.stock.repository;

import com.project.pos.stock.entity.StockCategory;
import com.project.pos.stock.entity.Stock;
import com.project.pos.stock.repositoryDSL.StockRepositoryCustom;
import com.project.pos.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockRepository extends JpaRepository<Stock, Long>, StockRepositoryCustom {
    @Query("select count(s) from Stock s " +
            "where s.stockCategory=:stockCategory and s.deleted=false")
    Long countStockByCategory(@Param("stockCategory") StockCategory stockCategory);

    @Query("select s.id from Stock s where s.store=:store and s.number=:number and s.deleted=false")
    Long countStockByNumber(@Param("store") Store store, @Param("number") Long number);
}
