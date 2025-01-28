package com.example.toyauth.app.core.util;

import com.example.toyauth.app.core.dto.JwtDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
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
                .grantType("USER")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String generateToken(Authentication authentication, Long expirationFromCreated) {
        Claims claims = generateTokenClaims(authentication);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(getTokenExpiresIn(expirationFromCreated))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    private Claims generateTokenClaims(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        //MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

        String authorization = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        claims.put("auth", authorization);
        claims.put("id", 1L);
    /*    String provider = determineProvider(authentication);
        claims.put("provider", provider);*/


        return claims;
    }

    private Date getTokenExpiresIn(long nowToAfterSecond) {
        long now = new Date().getTime();
        return new Date(now + nowToAfterSecond);
    }
}
