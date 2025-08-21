package com.example.toyauth.app.auth.controller;

import com.example.toyauth.app.auth.domain.dto.LoginRequestDto;
import com.example.toyauth.app.auth.service.AuthService;
import com.example.toyauth.app.common.handler.LoginSuccessHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Tag(name = "AuthController", description = "사용자 로그인")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "oauth2")
@RequestMapping
public class AuthController {

    private final AuthService authService;
    private final LoginSuccessHandler loginSuccessHandler;

    @PostMapping("/login")
    @Operation(summary = "사용자 로그인",
            tags = "AuthController")
    public void login(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        Authentication authentication = authService.login(loginRequestDto);

        loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
    }

    @GetMapping("/oauth2/authorization/github")
    @Operation(summary = "깃헙 oauth2 로그인",
            tags = "AuthController")
    public void githubLogin() {}





}
