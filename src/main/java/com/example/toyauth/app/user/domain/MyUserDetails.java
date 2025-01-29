package com.example.toyauth.app.user.domain;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@Getter
public class MyUserDetails implements UserDetails, OAuth2User {

    private final User user;
    private final Map<String, Object> attributes;

    // 표준 사용자 이름/비밀번호 인증을 위한 생성자
    public MyUserDetails(User user) {
        this.user = user;
        this.attributes =  Collections.emptyMap(); // OAuth 사용자가 아닌 경우 속성을 null로 설정
    }

    // OAuth2 인증을 위한 생성자
    public MyUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = (attributes == null) ? Collections.emptyMap() : attributes;
    }

    @Override
    public String getName() {
        return user.getEmail();
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
        return user.getEmail();
    }
}
