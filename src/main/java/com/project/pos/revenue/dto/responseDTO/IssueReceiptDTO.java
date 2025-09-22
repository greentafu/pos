package com.project.pos.revenue.dto.responseDTO;

import com.project.pos.revenue.dto.ReceiptDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IssueReceiptDTO {
    private List<ReceiptDTO> issueList;
    private List<ReceiptDTO> cancelList;
}
