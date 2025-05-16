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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider tokenProvider;
    private final RedisUserService redisUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println(">>>> JwtFilter 진입, URI: " + request.getRequestURI());
        String token = tokenProvider.resolveAccessToken(request);

        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            Long userId = tokenProvider.getUserId(token); // JWT에서 userId 추출

            // Redis에서 사용자 정보 조회 (없으면 DB에서 조회 후 Redis에 저장)
            MyUserDetails userDetails = redisUserService.getUserDetails(userId)
                    .orElseGet(() -> redisUserService.loadUserByDB(userId));

            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
                return;
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // 로그인, 유저, OAuth2 관련 API 및 Swagger 예외 처리
        return path.startsWith("/api/login")
                || path.equals("/api/user")
                || path.startsWith("/api/oauth2")
                || path.startsWith("/api/swagger-ui/")
                || path.startsWith("/api/v3/api-docs")
                || path.startsWith("/api/swagger-resources/")
                || path.startsWith("/api/webjars/");
    }


}

