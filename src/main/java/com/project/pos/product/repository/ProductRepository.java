package com.project.pos.product.repository;

import com.project.pos.product.entity.ProductCategory;
import com.project.pos.product.entity.Product;
import com.project.pos.product.repositoryDSL.ProductRepositoryCustom;
import com.project.pos.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    @Query("select count(p) from Product p " +
            "where p.productCategory=:productCategory and p.deleted=false")
    Long countProductByCategory(@Param("productCategory") ProductCategory productCategory);
    @Query("select p.id from Product p where p.store=:store and p.number=:number and p.deleted=false")
    Long countProductByNumber(@Param("store") Store store, @Param("number") Long number);
}
