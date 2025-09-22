package com.project.pos.store.repository;

import com.project.pos.store.entity.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DayOfWeekRepository extends JpaRepository<DayOfWeek, Long> {
}
