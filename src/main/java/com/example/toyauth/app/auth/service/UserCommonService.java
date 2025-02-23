package com.example.toyauth.app.auth.service;

import com.example.toyauth.app.auth.domain.MyUserDetails;
import com.example.toyauth.app.common.enumuration.Provider;
import com.example.toyauth.app.common.exception.RestApiException;
import com.example.toyauth.app.common.exception.impl.UserErrorCode;
import com.example.toyauth.app.user.domain.User;
import com.example.toyauth.app.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class UserCommonService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User loginUser = userRepository.findByUsernameAndProvider(username, Provider.COMMON)
                .orElseThrow(() -> new RestApiException(UserErrorCode.USER_NOT_EXIST));

        return MyUserDetails.fromUser(loginUser);
    }
}
