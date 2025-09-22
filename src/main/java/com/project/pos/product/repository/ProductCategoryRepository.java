package com.project.pos.product.repository;

import com.project.pos.product.entity.ProductCategory;
import com.project.pos.product.repositoryDSL.ProductCategoryRepositoryCustom;
import com.project.pos.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>, ProductCategoryRepositoryCustom {
    @Query("select pc from ProductCategory pc " +
            "where pc.store=:store and pc.deleted=false and pc.arrangement is null " +
            "order by pc.id asc")
    List<ProductCategory> getProductCategoryNonArrayList(@Param("store") Store store);
    @Query("select pc from ProductCategory pc " +
            "where pc.store=:store and pc.deleted=false and pc.arrangement is not null " +
            "order by pc.arrangement asc")
    List<ProductCategory> getProductCategoryArrayList(@Param("store") Store store);

    @Query("select pc from ProductCategory pc " +
            "where pc.store=:store and pc.deleted=false " +
            "order by pc.id asc")
    List<ProductCategory> getProductCategoryListByStore(@Param("store") Store store);
    @Query("select pc.id from ProductCategory pc " +
            "where pc.store=:store and pc.deleted=false and pc.arrangement=1")
    Optional<Long> getFirstProductCategory(@Param("store") Store store);
}
