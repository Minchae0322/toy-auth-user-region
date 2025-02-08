package com.example.toyauth.app.token.repository;

import com.example.toyauth.app.token.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
}
