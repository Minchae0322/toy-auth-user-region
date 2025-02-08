package com.example.toyauth.app.token.repository;

import com.example.toyauth.app.token.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {
    Optional<RefreshToken> findByToken(String refreshToken);
    Optional<RefreshToken> findByUsername(String username);
    void save(RefreshToken refreshToken);
    void deleteByUsername(String username);
}
