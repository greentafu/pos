package com.project.pos.option.repository;

import com.project.pos.option.dto.queryDTO.OptionQuantityDTO;
import com.project.pos.option.entity.Option;
import com.project.pos.option.entity.OptionReceipt;
import com.project.pos.option.entity.OptionReceiptID;
import com.project.pos.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OptionReceiptRepository extends JpaRepository<OptionReceipt, OptionReceiptID> {
    @Modifying
    @Transactional
    @Query("delete from OptionReceipt o where o.id.optionKey=:optionKey")
    void deleteOptionReceiptByOptionKey(@Param("optionKey")Long optionKey);

    @Query("select o from OptionReceipt o " +
            "where o.option=:option " +
            "order by o.id asc")
    List<OptionReceipt> getOptionReceiptByOption(@Param("option") Option option);

    @Query("select count(o) from OptionReceipt o where o.stock=:stock")
    Long countOptionReceiptByStock(@Param("stock") Stock stock);

    @Query("select o.option.id, o.stock.stockCost, o.quantity from OptionReceipt o" +
            " where o.stock=:stock")
    List<OptionQuantityDTO> getOptionListByStock(@Param("stock") Stock stock);

    @Query("select distinct o.option from OptionReceipt o " +
            "where o.stock=:stock and o.option.soldOutType=true and o.option.soldOut=:soldOut")
    List<Option> getOptionSoldOut(@Param("stock") Stock stock, @Param("soldOut") Boolean soldOut);
}
