package com.example.toyauth.app.user.controller;


import com.example.toyauth.app.auth.domain.MyUserDetails;
import com.example.toyauth.app.auth.domain.dto.LoginRequestDto;
import com.example.toyauth.app.user.domain.dto.UserCreateDto;
import com.example.toyauth.app.user.domain.dto.UserDto;
import com.example.toyauth.app.user.domain.dto.UserPasswordChangeDto;
import com.example.toyauth.app.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserController", description = "사용자 로그인")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    @PostMapping("")
    @Operation(summary = "사용자 생성",
            tags = "UserController")
    public ResponseEntity<Long> createUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return ResponseEntity.ok(userService.createUser(userCreateDto));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "단일 사용자 조회",
            tags = "UserController")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping(value = "/{userId}/password")
    @Operation(summary = "비밀번호 변경", tags = "UserController")
    public ResponseEntity<Long> changePassword(
            @Valid @RequestBody UserPasswordChangeDto userPasswordChangeDto,
            @AuthenticationPrincipal MyUserDetails myUserDetails) {

        return ResponseEntity.ok(userService.changePassword(myUserDetails.getUser().getId(), userPasswordChangeDto));
    }

}
