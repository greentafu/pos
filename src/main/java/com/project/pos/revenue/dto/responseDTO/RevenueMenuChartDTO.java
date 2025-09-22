package com.project.pos.revenue.dto.responseDTO;

import com.project.pos.revenue.dto.queryDTO.RevenueChartDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RevenueMenuChartDTO {
    private List<RevenueChartDTO> mainList;
    private List<RevenueChartDTO> subList;
}
