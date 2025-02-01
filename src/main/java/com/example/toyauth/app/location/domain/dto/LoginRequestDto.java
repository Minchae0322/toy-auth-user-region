package com.example.toyauth.app.location.domain.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

        @Email(message = "이메일 형식에 맞지 않습니다.")
        String email,

        @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
        String password
)
{
}
