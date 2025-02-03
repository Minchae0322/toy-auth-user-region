package com.example.toyauth.app.user.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserEncodeDto {

    private String encryptedPassword;
}
