package com.example.toyauth.app.common.filter;

import com.example.toyauth.app.auth.domain.MyUserDetails;
import com.example.toyauth.app.common.util.JwtProvider;
import com.example.toyauth.app.user.service.RedisUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider tokenProvider;
    private final RedisUserService redisUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);

        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            Long userId = tokenProvider.getUserId(token); // JWT에서 userId 추출

            // Redis에서 사용자 정보 조회 (없으면 DB에서 조회 후 Redis에 저장)
            MyUserDetails userDetails = redisUserService.getUserDetails(userId)
                    .orElseGet(() -> redisUserService.loadUserByDB(userId));

            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // 필터를 제외할 경로 패턴 ("/oauth2/*" 포함)
        return path.matches("^/(login|user|oauth2(/.*)?)$");
    }


    // Authorization 헤더에서 JWT 토큰 추출 (Bearer 스키마 사용)
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }




}

