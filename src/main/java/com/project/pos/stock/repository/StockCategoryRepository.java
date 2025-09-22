package com.project.pos.stock.repository;

import com.project.pos.stock.entity.StockCategory;
import com.project.pos.stock.repositoryDSL.StockCategoryRepositoryCustom;
import com.project.pos.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockCategoryRepository extends JpaRepository<StockCategory, Long>, StockCategoryRepositoryCustom {
    @Query("select sc from StockCategory sc " +
            "where sc.store=:store and sc.deleted=false " +
            "order by sc.id asc")
    List<StockCategory> getStockCategoryList(@Param("store") Store store);
}
