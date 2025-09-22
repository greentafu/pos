package com.project.pos.store.dto.responseDTO;

import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.dto.StoreTimeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StorePageDTO {
    private StoreDTO storeDTO;
    private List<StoreTimeDTO> timeDTOList;
}
