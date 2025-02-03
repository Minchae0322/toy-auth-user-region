package com.example.toyauth.app.user.service;

import com.example.toyauth.app.common.exception.RestApiException;
import com.example.toyauth.app.common.exception.impl.UserErrorCode;
import com.example.toyauth.app.user.domain.User;
import com.example.toyauth.app.user.domain.dto.UserCreateDto;
import com.example.toyauth.app.user.domain.dto.UserDto;
import com.example.toyauth.app.user.domain.dto.UserEncodeDto;
import com.example.toyauth.app.user.domain.dto.UserUpdateDto;
import com.example.toyauth.app.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Long createUser(UserCreateDto userCreateDto) {
        User user = userCreateDto.toEntity();

        UserEncodeDto userEncodeDto = getUserEncodeDto(userCreateDto);
        user.encode(userEncodeDto);

        return userRepository.save(user).getId();
    }


    public UserDto getUser(Long userId) {
        User user = findUserByIdAndActivated(userId);

        return UserDto.of(user);
    }

    public UserDto updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User user = findUserByIdAndActivated(userId);
        return UserDto.of(user);
    }


    private UserEncodeDto getUserEncodeDto(UserCreateDto userCreateDto) {
        return UserEncodeDto.builder()
                .encryptedPassword(passwordEncoder.encode(userCreateDto.password()))
                .build();
    }

    private User findUserByIdAndActivated(Long userId) {
        return userRepository.findByIdAndActivated(userId, true)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_EXIST));
    }


}
