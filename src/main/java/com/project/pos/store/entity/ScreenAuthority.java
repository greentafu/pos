package com.project.pos.store.entity;

import com.project.pos.employee.entity.JobTitle;
import com.project.pos.home.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScreenAuthority extends BaseEntity {
    @EmbeddedId
    private ScreenAuthorityID id;
    @Column(nullable = false)
    @Builder.Default
    private Boolean authority = false;

    @MapsId("jobTitleKey")
    @ManyToOne
    @JoinColumn(name = "job_title_id", nullable = false)
    private JobTitle jobTitle;
    @MapsId("screenKey")
    @ManyToOne
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    public ScreenAuthority(JobTitle jobTitle, Screen screen, Boolean authority) {
        this.id = new ScreenAuthorityID(jobTitle.getId(), screen.getId());
        this.jobTitle = jobTitle;
        this.screen = screen;
        this.authority = authority;
    }
}
