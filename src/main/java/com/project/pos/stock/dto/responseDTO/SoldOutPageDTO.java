package com.project.pos.stock.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SoldOutPageDTO {
    private SoldOutDTO nonSoldOutDTO;
    private SoldOutDTO soldOutDTO;
}
