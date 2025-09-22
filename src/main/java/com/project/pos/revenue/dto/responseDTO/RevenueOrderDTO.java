package com.project.pos.revenue.dto.responseDTO;

import com.project.pos.revenue.dto.queryDTO.RevenueOrderPageDTO;
import com.project.pos.revenue.dto.queryDTO.RevenueOrderSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class RevenueOrderDTO {
    private Page<RevenueOrderPageDTO> pageDTO;
    private RevenueOrderSummaryDTO summaryDTO;
}
