package com.example.toyauth.app.user.service;

import com.example.toyauth.app.common.exception.RestApiException;
import com.example.toyauth.app.common.exception.impl.UserErrorCode;
import com.example.toyauth.app.user.domain.User;
import com.example.toyauth.app.user.domain.dto.*;
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


    public UserDto.Get getUser(Long userId) {
        User user = findUserByIdAndActivated(userId);

        return UserDto.Get.of(user);
    }

    public UserDto.Get updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User user = findUserByIdAndActivated(userId);

        user.update(userUpdateDto);

        return UserDto.Get.of(user);
    }

    public Long changePassword(Long userId, UserPasswordChangeDto passwordChangeDto) {
        User user = findUserByIdAndActivated(userId);

        if(!passwordEncoder.matches(passwordChangeDto.oldPassword(), user.getPassword())) {
            throw new RestApiException(UserErrorCode.USER_NOT_EXIST);
        }

        UserEncodeDto userEncodeDto = getUserEncodeDto(passwordChangeDto);
        user.encode(userEncodeDto);

        return userRepository.save(user).getId();
    }


    private UserEncodeDto getUserEncodeDto(EncodableDto encodableDto) {
        return UserEncodeDto.builder()
                .encryptedPassword(passwordEncoder.encode(encodableDto.getPassword()))
                .build();
    }

    private User findUserByIdAndActivated(Long userId) {
        return userRepository.findByIdAndActivated(userId, true)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_EXIST));
    }


}
