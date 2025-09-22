package com.project.pos.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScreenArrangement {
    @EmbeddedId
    private ScreenArrangementID id;
    @Column(nullable = false)
    private Integer page;
    @Column(nullable = false)
    private Integer indexValue;

    @MapsId("screenKey")
    @ManyToOne
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;
    @MapsId("storeKey")
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public ScreenArrangement(Store store, Screen screen, Integer page, Integer indexValue) {
        this.id = new ScreenArrangementID(store.getId(), screen.getId());
        this.page = page;
        this.indexValue = indexValue;
    }
}
