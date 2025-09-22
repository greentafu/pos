package com.project.pos.revenue.dto.responseDTO;

import com.project.pos.revenue.dto.queryDTO.RevenueMenuPageDTO;
import com.project.pos.revenue.dto.queryDTO.RevenueMenuSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class RevenueMenuDTO {
    private Page<RevenueMenuPageDTO> pageDTO;
    private RevenueMenuSummaryDTO summaryDTO;
}
