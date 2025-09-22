package com.project.pos.dslDTO;

import com.project.pos.store.dto.StoreDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@Data
public class SoldOutListRequestDTO {
    public StoreDTO storeDTO;
    private Long searchCategory;
    private Boolean soldOutType;
}
