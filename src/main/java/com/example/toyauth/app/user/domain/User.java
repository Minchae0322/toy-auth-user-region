package com.example.toyauth.app.user.domain;

import com.example.toyauth.app.common.enumuration.Provider;
import com.example.toyauth.app.common.enumuration.Role;
import com.example.toyauth.app.user.domain.dto.UserEncodeDto;
import com.example.toyauth.app.user.domain.dto.UserUpdateDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.redis.core.RedisHash;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(unique = true)
    @Comment("이메일 혹은 username (로그인 ID 역할)")
    private String username;

    @Column()
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
    private boolean activated = true;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    @Comment("OAuth2 제공자 (google, github, naver 등)")
    private Provider provider;

    @Column
    @Comment("OAuth2 제공자에서 받은 사용자 ID")
    private String providerId;

    @Column(updatable = false)
    @Comment("계정 생성일")
    private LocalDateTime createdAt;

    @Column
    @Comment("계정 정보 수정일")
    private LocalDateTime updatedAt;

    @Column(name = "profile_img_id")
    @Comment("사용자 프로필 이미지 첨부파일 아이디")
    private Long profileImgId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserRegion> userRegions;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void encode(UserEncodeDto userEncodeDto) {
        this.password = userEncodeDto.getEncryptedPassword();
    }

    public void update(UserUpdateDto userUpdateDto) {
        if (Objects.nonNull(userUpdateDto.nickname())) {
            this.nickname = userUpdateDto.nickname();
        }

        if (Objects.nonNull(userUpdateDto.profileImgId())) {
            this.profileImgId = userUpdateDto.profileImgId();
        }
    }
}