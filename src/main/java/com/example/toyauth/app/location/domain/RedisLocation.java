package com.example.toyauth.app.location.domain;

import com.example.toyauth.app.location.domain.dto.KakaoSearchLocationResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "kakao_search_location")
public class RedisLocation {

    @Id
    private String locationId;

    @Indexed
    private KakaoSearchLocationResponse data;

    @TimeToLive
    private long ttl;
}
