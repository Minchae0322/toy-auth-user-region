package com.example.toyauth.app.common.util;

import com.example.toyauth.app.auth.domain.MyUserDetails;
import com.example.toyauth.app.common.dto.JwtDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    public static final Long ACCESS_TOKEN_EXPIRATION = 60 * 60 * 1000L;
    public static final Long REFRESH_TOKEN_EXPIRATION = 30 * 24 * 60 * 60 * 1000L;

    private final Key key;

    public JwtProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public JwtDto generateAccessAndRefreshTokens(Authentication authentication) {
        String accessToken = generateToken(authentication, ACCESS_TOKEN_EXPIRATION);
        String refreshToken = generateToken(authentication, REFRESH_TOKEN_EXPIRATION);

        return JwtDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String generateToken(Authentication authentication, Long expirationFromCreated) {
        Claims claims = generateTokenClaims((MyUserDetails) authentication.getPrincipal());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(getTokenExpiresIn(expirationFromCreated))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(MyUserDetails userDetails, Long expirationFromCreated) {
        Claims claims = generateTokenClaims(userDetails);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(getTokenExpiresIn(expirationFromCreated))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    private Claims generateTokenClaims(MyUserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getName());

        String authorization = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        claims.put("auth", authorization);
        claims.put("username", userDetails.getUser().getUsername());
        claims.put("userId", userDetails.getUser().getId());
        String provider = userDetails.getUser().getProvider().getValue();
        claims.put("provider", provider);
        claims.put("role", authorization);

        return claims;
    }


    private Date getTokenExpiresIn(long nowToAfterSecond) {
        long now = new Date().getTime();
        return new Date(now + nowToAfterSecond);
    }

    public boolean validateToken(String token) {
        try {
            // JWT 토큰 파싱 (유효한 서명 및 형식인지 검사)
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            // JWT 서명이 올바르지 않거나, 토큰 형식이 잘못된 경우
            // ex) 위조된 토큰
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우
        } catch (UnsupportedJwtException e) {
            // 지원하지 않는 JWT 토큰인 경우
        } catch (IllegalArgumentException e) {
            // JWT 토큰이 비어있거나 null인 경우
        }
        return false;
    }



    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return claims.get("userId", Long.class);
    }

    public String getUsername(String token) {
        Claims claims = parseClaims(token);
        return claims.get("username", String.class);
    }

    public String getRole(String token) {
        Claims claims = parseClaims(token);
        return claims.get("auth", String.class); // 여러 권한이 있을 경우, ","로 구분됨
    }

    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims(); // 만료된 토큰에서도 Claims는 가져올 수 있음
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT Token", e);
        }
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        Optional<Cookie> refreshTokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst();

        return refreshTokenCookie.map(Cookie::getValue).orElse(null);
    }
}
