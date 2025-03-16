package com.example.toyauth.app.common.filter;

import com.example.toyauth.app.auth.domain.MyUserDetails;
import com.example.toyauth.app.auth.domain.RefreshToken;
import com.example.toyauth.app.auth.repository.RefreshTokenRepository;
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
import java.util.Optional;

import static com.example.toyauth.app.common.util.JwtProvider.ACCESS_TOKEN_EXPIRATION;

@Component
@RequiredArgsConstructor
public class RefreshJwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final RedisUserService redisUserService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtProvider.resolveAccessToken(request);

        if (accessToken != null && jwtProvider.validateToken(accessToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = jwtProvider.resolveRefreshToken(request);

        if (refreshToken != null && jwtProvider.validateToken(refreshToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (StringUtils.hasText(refreshToken) && jwtProvider.validateToken(refreshToken)) {
            Optional<RefreshToken> optionalToken = refreshTokenRepository.findByToken(refreshToken);

            optionalToken.ifPresent(storedToken -> {
                Long userId = storedToken.getUserId();

                MyUserDetails userDetails = redisUserService.getUserDetails(userId)
                        .orElseGet(() -> redisUserService.loadUserByDB(userId));

                String newAccessToken = jwtProvider.generateToken(userDetails, ACCESS_TOKEN_EXPIRATION);

                response.setHeader("Authorization", "Bearer " + newAccessToken);
                response.setStatus(HttpServletResponse.SC_OK);
            });


            if (optionalToken.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

        }
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // 로그인, 유저, OAuth2 관련 API 및 Swagger 예외 처리
        return path.startsWith("/api/login")
                || path.startsWith("/api/oauth2")
                || path.startsWith("/api/swagger-ui/")
                || path.startsWith("/api/v3/api-docs")
                || path.startsWith("/api/swagger-resources/")
                || path.startsWith("/api/webjars/");
    }




}
