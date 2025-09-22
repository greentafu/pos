package com.project.pos.store.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MailPageDTO {
    private Long id;
    private String name;
    private LocalDateTime modDate;
    private String notes;
}
