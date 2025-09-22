package com.project.pos.revenue.repositoryDSL;

public interface OrderItemRepositoryCustom {
    Long getOptionAmount(Long id);
    Long getPerBasic(Long id);
    Long getPerPercent(Long id);
    Long getAllBasic(Long id);
    Long getAllPercent(Long id);
}
