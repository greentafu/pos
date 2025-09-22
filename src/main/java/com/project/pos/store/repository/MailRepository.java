package com.project.pos.store.repository;

import com.project.pos.store.entity.Mail;
import com.project.pos.store.repositoryDSL.MailRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<Mail, Long>, MailRepositoryCustom {
}
