package com.project.pos.employee.dto.responseDTO;

import com.project.pos.employee.dto.JobTitleDTO;
import com.project.pos.store.dto.ScreenAuthorityDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class JobTitleScreenAuthDTO {
    private JobTitleDTO jobTitleDTO;
    private List<ScreenAuthorityDTO> screenAuthorityDTOList;
}
