package com.project.pos.option.repository;

import com.project.pos.option.entity.OptionCategory;
import com.project.pos.option.repositoryDSL.OptionCategoryRepositoryCustom;
import com.project.pos.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OptionCategoryRepository extends JpaRepository<OptionCategory, Long>, OptionCategoryRepositoryCustom {
    @Query("select oc from OptionCategory oc " +
            "where oc.store=:store and oc.deleted=false and oc.arrangement is null " +
            "order by oc.id asc")
    List<OptionCategory> getOptionCategoryNonArrayList(@Param("store") Store store);
    @Query("select oc from OptionCategory oc " +
            "where oc.store=:store and oc.deleted=false and oc.arrangement is not null " +
            "order by oc.arrangement asc")
    List<OptionCategory> getOptionCategoryArrayList(@Param("store") Store store);

    @Query("select oc from OptionCategory oc " +
            "where oc.store=:store and oc.deleted=false " +
            "order by oc.id asc")
    List<OptionCategory> getOptionCategoryListByStore(@Param("store") Store store);
}
