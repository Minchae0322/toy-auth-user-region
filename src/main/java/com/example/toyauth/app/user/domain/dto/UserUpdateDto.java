package com.example.toyauth.app.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.Comment;

public record UserUpdateDto(

        @Schema(title = "변경 닉네임", description = "변경 닉네임")
        String nickname,

        @Positive(message = "프로필 이미지 아이디는 양수의 값이어야 합니다.")
        @Schema(title = "프로필 이미지 아이디", description = "프로필 이미지 아이디")
        Long profileImgId
) {
}
