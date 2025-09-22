package com.project.pos.store.entity;

import com.project.pos.home.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StoreTime extends BaseEntity {
    @EmbeddedId
    private StoreTimeID id;
    @Column(nullable = false)
    private String startTime;
    @Column(nullable = false)
    private String endTime;
    @Column(nullable = false)
    private Boolean statusValue;

    @MapsId("storeKey")
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @MapsId("dayOfWeekKey")
    @ManyToOne
    @JoinColumn(name = "day_of_week_id", nullable = false)
    private DayOfWeek dayOfWeek;

    public StoreTime(Store store, DayOfWeek dayOfWeek, String startTime, String endTime, Boolean statusValue) {
        this.id = new StoreTimeID(store.getId(), dayOfWeek.getId());
        this.startTime = startTime;
        this.endTime = endTime;
        this.statusValue = statusValue;
    }
}
