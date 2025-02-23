package com.example.toyauth.app.user.domain.dto;

import com.example.toyauth.app.common.enumuration.Provider;
import com.example.toyauth.app.common.enumuration.Role;
import com.example.toyauth.app.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserDto {

    @Schema(description = "사용자 아이디")
    private Long userId;

    @Schema(description = "사용자 닉네임")
    private String nickname;

    @Schema(description = "유저 권한")
    private Role role;

    @Schema(description = "로그인 플랫폼")
    private Provider provider;

    @Schema(description = "계정 생성 일")
    private LocalDateTime createdAt;

    @Schema(description = "계정 정보 수정일")
    private LocalDateTime updatedAt;


    public static UserDto of(User user) {
        return UserDto.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .role(user.getRole())
                .provider(user.getProvider())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
