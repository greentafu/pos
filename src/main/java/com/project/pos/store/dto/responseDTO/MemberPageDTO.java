package com.project.pos.store.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MemberPageDTO {
    private Long id;
    private String phoneNumber;
    private LocalDateTime modDate;
    private Long points;
}
