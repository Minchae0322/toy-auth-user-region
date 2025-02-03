package com.example.toyauth.app.auth.controller;

import com.example.toyauth.app.auth.domain.dto.LoginRequestDto;
import com.example.toyauth.app.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AuthController", description = "사용자 로그인")
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "oauth2")
@RequestMapping
public class AuthController {
    private final AuthService authService;

    @GetMapping("/login")
    @Operation(summary = "사용자 로그인",
            tags = "AuthController")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @GetMapping("/oauth2/authorization/github")
    @Operation(summary = "깃헙 oauth2 로그인",
            tags = "AuthController")
    public void githubLogin() {}




}
