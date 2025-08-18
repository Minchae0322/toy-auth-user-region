package com.example.toyauth.app.config;

import com.example.toyauth.app.auth.repository.RefreshTokenRepository;
import com.example.toyauth.app.auth.service.OAuth2Service;
import com.example.toyauth.app.common.filter.ExternalAuthenticationFilter;
import com.example.toyauth.app.common.filter.JwtFilter;
import com.example.toyauth.app.common.filter.RefreshJwtFilter;
import com.example.toyauth.app.common.handler.LoginFailureHandler;
import com.example.toyauth.app.common.handler.LoginSuccessHandler;
import com.example.toyauth.app.common.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.example.toyauth.app.common.constants.GlobalConstants.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final Oauth2Properties oauth2Properties;
    private final OAuth2Service oAuth2Service;

    private final JwtFilter jwtFilter;
    private final RefreshJwtFilter refreshJwtFilter;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ExternalAuthenticationFilter externalAuthenticationFilter;

    /**
     * 내부 API용 SecurityFilterChain (API Key 인증)
     */
    @Bean
    @Order(1)
    public SecurityFilterChain internalApiFilterChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher("/api/external/**")
            .addFilterBefore(externalAuthenticationFilter, BasicAuthenticationFilter.class)
            .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                .anyRequest().authenticated() // 모든 내부 API는 인증 필요
            )
            .build();
    }

    /**
     * 기존 JWT 인증용 SecurityFilterChain
     * @Order(2) - 내부 API가 아닌 모든 요청 처리
     */
    @Bean
    @Order(2)
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
        return http
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(refreshJwtFilter, JwtFilter.class)
            .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(AbstractHttpConfigurer::disable)
            .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                .successHandler(new LoginSuccessHandler(jwtProvider, oauth2Properties, refreshTokenRepository))
                .failureHandler(new LoginFailureHandler())
                .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig.userService(oAuth2Service))
            )
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin(HTTP_HTTPS + "://localhost:5173");
        configuration.addAllowedOrigin(HTTP_HTTPS + "://" + SERVER_URL);
        configuration.addAllowedOrigin(HTTP_HTTPS +"://localhost:8081");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader(ACCESS_TOKEN_HEADER_NAME);
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}