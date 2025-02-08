package com.example.toyauth.app.token.repository.impl;

import com.example.toyauth.app.token.domain.RefreshToken;
import com.example.toyauth.app.token.repository.JpaRefreshTokenJpaRepository;
import com.example.toyauth.app.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaRefreshTokenRepository implements RefreshTokenRepository {

    private final JpaRefreshTokenJpaRepository jpaRepository;

    @Override
    public Optional<RefreshToken> findByToken(String refreshToken) {
        return Optional.empty();
    }

    @Override
    public Optional<RefreshToken> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public void save(RefreshToken refreshToken) {
        jpaRepository.save(refreshToken);
    }

    @Override
    public void deleteByUsername(String username) {

    }
}

