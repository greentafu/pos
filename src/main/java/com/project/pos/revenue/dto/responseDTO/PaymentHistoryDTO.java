package com.project.pos.revenue.dto.responseDTO;

import com.project.pos.store.dto.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaymentHistoryDTO {
    private MemberDTO memberDTO;
    private List<PaymentReceiptDTO> methodReceiptList;
}
