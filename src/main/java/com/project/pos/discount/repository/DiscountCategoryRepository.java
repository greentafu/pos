package com.project.pos.discount.repository;

import com.project.pos.discount.entity.DiscountCategory;
import com.project.pos.discount.repositoryDSL.DiscountCategoryRepositoryCustom;
import com.project.pos.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiscountCategoryRepository extends JpaRepository<DiscountCategory, Long>, DiscountCategoryRepositoryCustom {
    @Query("select dc from DiscountCategory dc " +
            "where dc.store=:store and dc.deleted=false and dc.arrangement is null " +
            "order by dc.id asc")
    List<DiscountCategory> getDiscountCategoryNonArrayList(@Param("store") Store store);
    @Query("select dc from DiscountCategory dc " +
            "where dc.store=:store and dc.deleted=false and dc.arrangement is not null " +
            "order by dc.arrangement asc")
    List<DiscountCategory> getDiscountCategoryArrayList(@Param("store") Store store);

    @Query("select dc from DiscountCategory dc " +
            "where dc.store=:store and dc.deleted=false " +
            "order by dc.id asc")
    List<DiscountCategory> getDiscountCategoryList(@Param("store") Store store);
}
