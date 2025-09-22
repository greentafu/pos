package com.project.pos.dslDTO;

import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
    private int page;
    private int size;

    public PageRequestDTO(){
        this.page=1;
        this.size=15;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page-1, size, sort);
    }

    public StoreDTO storeDTO;
    public PosDTO posDTO;

    // 검색
    private String searchText;
    private Long searchCategory;

    private Long selectedId;

    // revenue
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long posNumber;
    private String receiptNumber;
    private Integer type;
}
