package com.example.toyauth.app.user.domain;

import com.example.toyauth.app.region.domain.Region;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_user_region")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_region_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "region_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Region region;
}
