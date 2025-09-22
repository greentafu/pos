package com.project.pos.revenue.dto;

import com.project.pos.product.dto.ProductDTO;
import com.project.pos.store.dto.StoreDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderItemDTO {
    private Long id;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    @NotNull(message = "{notNull.price}")
    private Long productPrice;
    @NotNull(message = "{notNull.cost}")
    private Long productCost;
    @NotNull(message = "{notNull.quantity}")
    private Long productCount;
    @NotNull(message = "{notNull.per_unit}")
    private Long productPerUnit;
    @NotNull(message = "{notNull.price_option}")
    private Long totalOption;
    @NotNull(message = "{notNull.price_discount}")
    private Long totalDiscount;
    @NotNull(message = "{notNull.price_total}")
    private Long totalPerUnit;
    @NotNull(message = "{notNull.price_total}")
    private Long totalPayment;
    private Long copyLocation;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
    @NotNull(message = "{notNull.product}")
    private ProductDTO productDTO;
    @NotNull(message = "{notNull.order}")
    private OrdersDTO ordersDTO;
}
