package com.example.toyauth.app.auth.domain;

import com.example.toyauth.app.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.*;

@Getter
@Builder
@RedisHash(value = "user", timeToLive = 7200)
public class MyUserDetails implements UserDetails, OAuth2User, Serializable {


    @Id
    @Comment("Redis Key")
    private final String userId;

    private final User user;

    @Builder.Default
    private final Map<String, Object> attributes = Collections.emptyMap();

    public static MyUserDetails fromOauth2User(User user, Map<String, Object> attributes) {
        return MyUserDetails.builder()
                .userId(String.valueOf(user.getId()))
                .user(user)
                .attributes(attributes)
                .build();
    }

    public static MyUserDetails fromUser(User user) {
        return MyUserDetails.builder()
                .userId(String.valueOf(user.getId()))
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

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
