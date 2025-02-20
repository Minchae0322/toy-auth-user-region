package com.example.toyauth.app.user.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;



public record UserPasswordChangeDto (

        @NotBlank(message = "이전 비밀번호는 공백 일 수 없습니다.")
        @Schema(title = "이전 비밀번호 ", description = "비밀번호")
        String oldPassword,

        @NotBlank(message = "새로운 비밀 번호는 공백 일 수 없습니다.")
        @Schema(title = "새 비밀번호 ", description = "새 비밀번호")
        String newPassword

) implements EncodableDto {

    @Override
    public String getPassword() {
        return newPassword;
    }
}
