package com.project.pos.store.repository;

import com.project.pos.employee.entity.JobTitle;
import com.project.pos.store.entity.ScreenAuthority;
import com.project.pos.store.entity.ScreenAuthorityID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ScreenAuthorityRepository extends JpaRepository<ScreenAuthority, ScreenAuthorityID> {
    @Query("select sa from ScreenAuthority sa where sa.jobTitle=:jobTitle")
    List<ScreenAuthority> screenAuthorityList(@Param("jobTitle")JobTitle jobTitle);

    @Modifying
    @Transactional
    @Query("delete from ScreenAuthority sa where sa.id.jobTitleKey=:jobTitleKey")
    void deleteScreenAuthorityByJobTitleKey(@Param("jobTitleKey")Long jobTitleKey);
}
