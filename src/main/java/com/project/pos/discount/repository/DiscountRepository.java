package com.project.pos.discount.repository;

import com.project.pos.discount.entity.Discount;
import com.project.pos.discount.entity.DiscountCategory;
import com.project.pos.discount.repositoryDSL.DiscountRepositoryCustom;
import com.project.pos.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long>, DiscountRepositoryCustom {
    @Query("select d from Discount d " +
            "where d.discountCategory.id=:searchCategory and d.deleted=false and d.arrangement is null " +
            "order by d.id asc")
    List<Discount> getDiscountNonArrayList(@Param("store") Store store, @Param("searchCategory") Long searchCategory);
    @Query("select d from Discount d " +
            "where d.discountCategory.id=:searchCategory and d.deleted=false and d.arrangement is not null " +
            "order by d.arrangement asc")
    List<Discount> getDiscountArrayList(@Param("store") Store store, @Param("searchCategory") Long searchCategory);
    
    @Query("select count(d) from Discount d " +
            "where d.discountCategory=:discountCategory and d.deleted=false")
    Long countDiscountByCategory(@Param("discountCategory")DiscountCategory discountCategory);
}
