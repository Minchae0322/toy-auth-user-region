package com.example.toyauth.app.user.domain.dto;

import com.example.toyauth.app.common.enumuration.Role;
import com.example.toyauth.app.common.util.RandomNicknameUtil;
import com.example.toyauth.app.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.Comment;


public record UserCreateDto(

        @NotBlank(message = "아이디는 공백 일 수 없습니다.")
        @Schema(title = "이메일 혹은 username (로그인 ID 역할)", description = "이메일 혹은 username (로그인 ID 역할)")
        String username,

        @NotBlank(message = "비밀번호는 공백 일 수 없습니다.")
        @Schema(title = "비밀번호 (일반 회원가입 시 사용)", description = "비밀번호")
        String password
) {
    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .role(Role.USER)
                .nickname(RandomNicknameUtil.generateRandomNickname())
                .provider("common")
                .activated(true)
                .build();
    }

}
