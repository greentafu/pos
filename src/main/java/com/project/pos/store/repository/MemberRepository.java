package com.project.pos.store.repository;

import com.project.pos.store.entity.Member;
import com.project.pos.store.entity.Store;
import com.project.pos.store.repositoryDSL.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    @Query("select m.id from Member m " +
            "where m.store=:store and m.phoneNumber=:phoneNumber and m.deleted=false")
    Long getMemberIdByPhoneNumber(@Param("store") Store store, @Param("phoneNumber") String phoneNumber);

    @Query("select m from Member m " +
            "where m.store=:store and m.phoneNumber=:phoneNumber and m.deleted=false")
    Member getMemberByPhoneNumber(@Param("store") Store store, @Param("phoneNumber") String phoneNumber);
}
