package com.example.toyauth.app.common.enumuration;

import com.example.toyauth.app.auth.domain.dto.OAuth2UserProfile;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

@Getter
public enum OAuthAttributes {
    NAVER("naver", (attribute) -> {
        Map<String, Object> userAttribute = (Map<String, Object>) attribute.get("response");
        return OAuth2UserProfile.builder()
                .id((String) userAttribute.get("id"))
                .attributes(userAttribute)
                .provider("naver")
                .build();
    }
    ),
    GOOGLE("google", (attribute) -> OAuth2UserProfile.builder()
            .id((String) attribute.get("sub"))
            .attributes(attribute)
            .provider("google")
            .build()
    ),
    GITHUB("github", (attribute) -> OAuth2UserProfile.builder()
            .id((String) attribute.get("login"))
            .attributes(attribute)
            .provider("github")
            .build()
    ),

    KAKAO("kakao", (attribute) -> OAuth2UserProfile.builder()
            .id(String.valueOf(attribute.get("id")))
            .attributes(attribute)
            .provider("kakao")
            .build()
    );

    private final String registrationId; // 로그인한 서비스(ex) google, naver..)
    private final Function<Map<String, Object>, OAuth2UserProfile> of; // 로그인한 사용자의 정보를 통하여 UserProfile을 가져옴

    OAuthAttributes(String registrationId, Function<Map<String, Object>, OAuth2UserProfile> of) {
        this.registrationId = registrationId;
        this.of = of;
    }

    public static OAuth2UserProfile extract(String registrationId, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(value -> registrationId.equals(value.registrationId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of.apply(attributes);
    }
}
