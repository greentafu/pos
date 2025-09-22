package com.project.pos.product.repository;

import com.project.pos.product.dto.queryDTO.ProductQuantityDTO;
import com.project.pos.product.entity.Product;
import com.project.pos.product.entity.ProductReceipt;
import com.project.pos.product.entity.ProductReceipt;
import com.project.pos.product.entity.ProductReceiptID;
import com.project.pos.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductReceiptRepository extends JpaRepository<ProductReceipt, ProductReceiptID> {
    @Modifying
    @Transactional
    @Query("delete from ProductReceipt p where p.id.productKey=:productKey")
    void deleteProductReceiptByProductKey(@Param("productKey")Long productKey);

    @Query("select p from ProductReceipt p " +
            "where p.product=:product " +
            "order by p.id asc")
    List<ProductReceipt> getProductReceiptByProduct(@Param("product") Product product);

    @Query("select count(p) from ProductReceipt p where p.stock=:stock")
    Long countProductReceiptByStock(@Param("stock") Stock stock);

    @Query("select pr.product.id, pr.stock.stockCost, pr.quantity from ProductReceipt pr " +
            "where pr.stock=:stock")
    List<ProductQuantityDTO> getProductListByStock(@Param("stock") Stock stock);

    @Query("select distinct pr.product from ProductReceipt pr " +
            "where pr.stock=:stock and pr.product.soldOutType=true and pr.product.soldOut=:soldOut")
    List<Product> getProductSoldOut(@Param("stock") Stock stock, @Param("soldOut") Boolean soldOut);
}
