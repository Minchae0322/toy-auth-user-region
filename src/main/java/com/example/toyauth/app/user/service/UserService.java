package com.example.toyauth.app.user.service;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

import com.example.toyauth.app.common.exception.RestApiException;
import com.example.toyauth.app.common.exception.impl.UserErrorCode;
import com.example.toyauth.app.external.controller.dto.UserExternalDto;
import com.example.toyauth.app.file.domain.AttachmentFile;
import com.example.toyauth.app.file.domain.dto.AttachmentFileDto;
import com.example.toyauth.app.file.repository.AttachmentFileRepository;
import com.example.toyauth.app.user.domain.User;
import com.example.toyauth.app.user.domain.dto.*;
import com.example.toyauth.app.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AttachmentFileRepository attachmentFileRepository;

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

    public UserExternalDto getUserForExternal(Long userId) {
        User user = findUserByIdAndActivated(userId);

        AttachmentFileDto profileFileDto = Optional.ofNullable(user.getProfileImgId())
            .flatMap(attachmentFileRepository::findById)
            .map(AttachmentFileDto::from)
            .orElse(null);

        return UserExternalDto.from(user, profileFileDto);
    }

    public List<UserExternalDto> getUsersForExternal(List<Long> userIds) {
        List<User> users = userRepository.findAllByIdInAndActivated(userIds, true);

        List<Long> profileImageIds = users.stream()
            .map(User::getProfileImgId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

        Map<Long, AttachmentFile> profileImageMap = profileImageIds.isEmpty()
            ? Collections.emptyMap()
            : attachmentFileRepository.findAllById(profileImageIds)
                .stream()
                .collect(toMap(AttachmentFile::getId, identity()));

        return users.stream()
            .map(user -> {
                AttachmentFileDto profileFileDto = Optional.ofNullable(user.getProfileImgId())
                    .map(profileImageMap::get)
                    .map(AttachmentFileDto::from)
                    .orElse(null);

                return UserExternalDto.from(user, profileFileDto);
            })
            .toList();
    }

    public Long resetPasswordAfterEmailVerification(Long userId, UserPasswordChangeDto passwordChangeDto) {
        User user = findUserByIdAndActivated(userId);

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
