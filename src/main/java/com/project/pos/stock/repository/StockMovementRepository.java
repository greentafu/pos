package com.project.pos.stock.repository;

import com.project.pos.stock.entity.StockMovement;
import com.project.pos.stock.repositoryDSL.StockMovementRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long>, StockMovementRepositoryCustom {
}
