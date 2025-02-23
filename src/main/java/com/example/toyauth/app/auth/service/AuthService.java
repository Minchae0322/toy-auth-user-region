package com.example.toyauth.app.auth.service;

import com.example.toyauth.app.auth.domain.MyUserDetails;
import com.example.toyauth.app.common.enumuration.Provider;
import com.example.toyauth.app.common.exception.RestApiException;
import com.example.toyauth.app.common.exception.impl.UserErrorCode;
import com.example.toyauth.app.location.domain.dto.KakaoSearchLocationResponse;
import com.example.toyauth.app.auth.domain.dto.LoginRequestDto;
import com.example.toyauth.app.user.domain.User;
import com.example.toyauth.app.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService  {

    private final AuthenticationManager authenticationManager;

    public Authentication login(LoginRequestDto loginRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDto.email(), loginRequestDto.password());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;
    }



}
