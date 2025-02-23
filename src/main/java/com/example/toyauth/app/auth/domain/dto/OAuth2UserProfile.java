package com.example.toyauth.app.auth.domain.dto;

import com.example.toyauth.app.common.enumuration.Provider;
import com.example.toyauth.app.common.enumuration.Role;
import com.example.toyauth.app.common.util.RandomNicknameUtil;
import com.example.toyauth.app.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuth2UserProfile {

    private String id; // 사용자 이름 또는 아이디

    private String provider; // 로그인한 서비스

    private Map<String, Object> attributes;

    public User toEntity() {
        return User.builder()
                .provider(Provider.ofProvider(provider))
                .providerId(id)
                .username(id)
                .role(Role.USER)
                .activated(true)
                .nickname(RandomNicknameUtil.generateRandomNickname())
                .build();
    }

}
