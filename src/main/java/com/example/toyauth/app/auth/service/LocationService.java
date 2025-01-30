package com.example.toyauth.app.auth.service;


import com.example.toyauth.app.auth.domain.dto.KakaoSearchLocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LocationService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey;

    private final RedisService redisService;

    public ResponseEntity<KakaoSearchLocationResponse> getLocationByKeyword(@RequestParam String query) {
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + query;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoSearchLocationResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                KakaoSearchLocationResponse.class
        );

        return response;

    }

}
