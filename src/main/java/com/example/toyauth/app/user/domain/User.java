package com.example.toyauth.app.user.domain;

import com.example.toyauth.app.core.enumuration.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, unique = true)
    @Comment("이메일 (로그인 ID 역할)")
    private String email;

    @Column(nullable = false)
    @Comment("비밀번호 (일반 회원가입 시 사용, OAuth 로그인 시 null 가능)")
    private String password;

    @Column
    @Comment("사용자 닉네임")
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Comment("사용자 역할 (ROLE_USER, ROLE_ADMIN)")
    private Role role;

    @Column(nullable = false)
    @Comment("계정 활성화 여부")
    private boolean activated;

    @Column
    @Comment("OAuth2 제공자 (google, github, naver 등)")
    private String provider;

    @Column
    @Comment("OAuth2 제공자에서 받은 사용자 ID")
    private String providerId;

    @Column(updatable = false)
    @Comment("계정 생성일")
    private LocalDateTime createdAt;

    @Column
    @Comment("계정 정보 수정일")
    private LocalDateTime updatedAt;

    @JoinColumn(name = "profile_img")
    @OneToMany
    private List<UserAttachmentFile> profileImg;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}