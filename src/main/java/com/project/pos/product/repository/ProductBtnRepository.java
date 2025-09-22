package com.project.pos.product.repository;

import com.project.pos.product.entity.Product;
import com.project.pos.product.entity.ProductBtn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductBtnRepository extends JpaRepository<ProductBtn, Long> {
    @Query("select pb from ProductBtn pb " +
            "where pb.product.productCategory.id=:category " +
            "order by pb.indexValue asc")
    List<ProductBtn> getProductBtnList(@Param("category") Long category);

    @Query("select pb.product.id from ProductBtn pb " +
            "where pb.product.productCategory.id=:category " +
            "order by pb.id asc")
    List<Long> getProductBtnIdList(@Param("category") Long category);

    @Query("select pb from ProductBtn pb where pb.product=:product")
    ProductBtn getProductBtnByProduct(@Param("product") Product product);

    @Modifying
    @Transactional
    @Query("delete from ProductBtn pb where pb.product=:product")
    void deleteProductBtnByProduct(@Param("product") Product product);
}
