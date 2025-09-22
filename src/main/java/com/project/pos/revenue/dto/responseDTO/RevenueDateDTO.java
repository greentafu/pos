package com.project.pos.revenue.dto.responseDTO;

import com.project.pos.revenue.dto.queryDTO.RevenueDatePageDTO;
import com.project.pos.revenue.dto.queryDTO.RevenueDateSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class RevenueDateDTO {
    private Page<RevenueDatePageDTO> pageDTO;
    private RevenueDateSummaryDTO summaryDTO;
}
