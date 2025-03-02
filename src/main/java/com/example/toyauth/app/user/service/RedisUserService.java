package com.example.toyauth.app.user.service;

import com.example.toyauth.app.auth.domain.MyUserDetails;
import com.example.toyauth.app.common.exception.RestApiException;
import com.example.toyauth.app.common.exception.impl.UserErrorCode;
import com.example.toyauth.app.user.repository.RedisUserRepository;
import com.example.toyauth.app.user.repository.UserRepository;
import com.example.toyauth.app.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisUserService {

    private final RedisUserRepository redisUserRepository;
    private final UserRepository userRepository;

    /**
     * Redis에서 사용자 정보 조회
     */
    public Optional<MyUserDetails> getUserDetails(Long userId) {
        return redisUserRepository.findById(String.valueOf(userId));
    }

    /**
     * 사용자 정보를 Redis에 저장 (캐싱)
     */
    public void saveUserDetails(MyUserDetails userDetails) {
        redisUserRepository.save(userDetails);
    }

    /**
     * 로그아웃 또는 권한 변경 시 Redis에서 사용자 정보 삭제
     */
    public void deleteUserDetails(Long userId) {
        redisUserRepository.deleteById(String.valueOf(userId));
    }

    /**
     * Redis에 없을 경우 DB에서 사용자 정보 조회 후 저장
     */
    public MyUserDetails loadUserByDB(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_EXIST));

        MyUserDetails userDetails = MyUserDetails.fromUser(user);
        saveUserDetails(userDetails);
        return userDetails;
    }
}
