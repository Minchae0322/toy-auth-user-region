package com.example.toyauth.app.core.dto;

import lombok.Getter;

@Getter
public class JwtDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
