package com.example.toyauth.app.config;

import com.example.toyauth.app.auth.repository.RefreshTokenRepository;
import com.example.toyauth.app.auth.service.OAuth2Service;
import com.example.toyauth.app.common.filter.JwtFilter;
import com.example.toyauth.app.common.filter.RefreshJwtFilter;
import com.example.toyauth.app.common.handler.LoginFailureHandler;
import com.example.toyauth.app.common.handler.LoginSuccessHandler;
import com.example.toyauth.app.common.util.JwtProvider;
import com.example.toyauth.app.user.service.RedisUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.example.toyauth.app.common.constants.GlobalConstants.*;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final OAuth2Service oAuth2Service;
    private final RedisUserService redisUserService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtFilter jwtFilter;
    private final RefreshJwtFilter refreshJwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(refreshJwtFilter, JwtFilter.class)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless 모드
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/login/**",
                                "/api/oauth2/**",
                                "/api/swagger-ui/**",
                                "/api/v3/api-docs/**",
                                "/api/swagger-resources/**",
                                "/api/swagger-ui.html",
                                "/api/webjars/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .successHandler(new LoginSuccessHandler(jwtProvider))
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