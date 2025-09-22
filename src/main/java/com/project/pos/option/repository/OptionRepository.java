package com.project.pos.option.repository;

import com.project.pos.option.entity.OptionCategory;
import com.project.pos.option.entity.Option;
import com.project.pos.option.repositoryDSL.OptionRepositoryCustom;
import com.project.pos.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long>, OptionRepositoryCustom {
    @Query("select o from Option o " +
            "where o.optionCategory.id=:searchCategory and o.deleted=false and o.arrangement is null " +
            "order by o.id asc")
    List<Option> getOptionNonArrayList(@Param("store") Store store, @Param("searchCategory") Long searchCategory);
    @Query("select o from Option o " +
            "where o.optionCategory.id=:searchCategory and o.deleted=false and o.arrangement is not null " +
            "order by o.arrangement asc")
    List<Option> getOptionArrayList(@Param("store") Store store, @Param("searchCategory") Long searchCategory);

    @Query("select count(o) from Option o " +
            "where o.optionCategory=:optionCategory and o.deleted=false")
    Long countOptionByCategory(@Param("optionCategory") OptionCategory optionCategory);
    @Query("select o.id from Option o where o.store=:store and o.number=:number and o.deleted=false")
    Long countOptionByNumber(@Param("store") Store store, @Param("number") Long number);
}
