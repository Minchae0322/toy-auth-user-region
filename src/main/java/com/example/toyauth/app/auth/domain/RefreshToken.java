package com.example.toyauth.app.auth.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@AllArgsConstructor
@Table(name = "tb_refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false, unique = true)
    private Long userId; // 특정 유저와 매핑

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiryDate);
    }

    public void renew(String refreshToken, Instant expiryDate) {
        this.token = refreshToken;
        this.expiryDate = expiryDate;

    }
}
