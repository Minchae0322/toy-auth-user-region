package com.example.toyauth.app.external.controller.dto;

import com.example.toyauth.app.common.enumuration.Provider;
import com.example.toyauth.app.common.enumuration.Role;
import com.example.toyauth.app.file.domain.dto.AttachmentFileDto;
import com.example.toyauth.app.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserExternalDto {

  @Schema(description = "사용자 아이디")
  private Long userId;

  @Schema(description = "사용자 닉네임")
  private String nickname;

  @Schema(description = "프로필 이미지 파일")
  private AttachmentFileDto profileImageFile;

  @Schema(description = "유저 권한")
  private Role role;

  @Schema(description = "로그인 플랫폼")
  private Provider provider;

  @Schema(description = "계정 활성화 여부")
  private boolean activated;


  public static UserExternalDto from(User user, AttachmentFileDto profileImageFileDto) {
    return UserExternalDto.builder()
        .userId(user.getId())
        .nickname(user.getNickname())
        .profileImageFile(profileImageFileDto)
        .role(user.getRole())
        .activated(user.isActivated())
        .provider(user.getProvider())
        .build();
  }

}

