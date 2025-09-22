package com.project.pos.store.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveMailDTO {
    private Long id;
    @NotBlank(message = "{notBlank.title}")
    private String title;
    private String notes;
    @NotBlank(message = "{notBlank.content}")
    private String content;
}
