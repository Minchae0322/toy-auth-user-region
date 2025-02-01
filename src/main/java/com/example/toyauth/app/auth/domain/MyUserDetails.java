package com.example.toyauth.app.auth.domain;

import com.example.toyauth.app.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
@Builder
public class MyUserDetails implements UserDetails, OAuth2User {

    private final User user;

    @Builder.Default
    private final Map<String, Object> attributes = Collections.emptyMap();

    //attributes 가 사용되지않으면 default 로 설정
    public static MyUserDetails fromOauth2User(User user, Map<String, Object> attributes) {
        return MyUserDetails.builder()
                .user(user)
                .attributes(attributes)
                .build();
    }

    public static MyUserDetails fromUser(User user) {
        return MyUserDetails.builder()
                .user(user)
                .build();
    }


    @Override
    public String getName() {
        return user.getUsername();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(user.getRole().getValue()));
        return auth;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
