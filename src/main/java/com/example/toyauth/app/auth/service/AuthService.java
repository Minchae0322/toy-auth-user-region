package com.example.toyauth.app.auth.service;

import com.example.toyauth.app.location.domain.dto.KakaoSearchLocationResponse;
import com.example.toyauth.app.auth.domain.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    public ResponseEntity<KakaoSearchLocationResponse> login(LoginRequestDto loginRequestDto) {
        return null;
    }
}
