package com.project.pos.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScreenDTO {
    private Long id;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    @NotBlank(message = "{notBlank.url-img}")
    private String imgUrl;
    @NotBlank(message = "{notBlank.url}")
    private String url;
}
