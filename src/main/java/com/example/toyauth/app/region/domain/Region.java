package com.example.toyauth.app.region.domain;

import com.example.toyauth.app.common.enumuration.RegionType;
import com.example.toyauth.app.user.domain.UserRegion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "tb_region")
public class Region {

    @Id
    @Column(name = "region_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_region_id")
    private Region parentRegion;

    @Column(nullable = false, length = 100)
    private String regionName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegionType regionType;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "parentRegion", cascade = CascadeType.ALL)
    private List<Region> subRegions;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    private List<UserRegion> memberRegions;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
