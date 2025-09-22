package com.project.pos.employee.dto.responseDTO;

import com.project.pos.employee.dto.queryDTO.WorkTimeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class PaymentSimplePageDTO {
    private Long id;
    private Long number;
    private String name;
    private Map<String, WorkTimeDTO> timeMap;
}
