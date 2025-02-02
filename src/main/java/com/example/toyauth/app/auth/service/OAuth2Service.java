package com.example.toyauth.app.auth.service;

import com.example.toyauth.app.auth.domain.dto.OAuth2UserProfile;
import com.example.toyauth.app.common.enumuration.OAuthAttributes;
import com.example.toyauth.app.auth.domain.MyUserDetails;
import com.example.toyauth.app.user.domain.User;
import com.example.toyauth.app.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = getOAuth2User(userRequest);
        OAuth2UserProfile oAuth2UserProfile = OAuthAttributes.extract(userRequest.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes());

        User user = findUserOrElseCreate(oAuth2UserProfile);

        return MyUserDetails.fromOauth2User(user, oAuth2UserProfile.getAttributes());
    }

    private OAuth2User getOAuth2User(OAuth2UserRequest userRequest) {
        return new DefaultOAuth2UserService().loadUser(userRequest);
    }

    private User findUserOrElseCreate(OAuth2UserProfile oAuth2UserProfile) {
        return userRepository.findByProviderIdAndProviderAndActivated(oAuth2UserProfile.getId(),
                        oAuth2UserProfile.getProvider(), true)
                .orElseGet(() -> userRepository.save(oAuth2UserProfile.toEntity()));
    }
}

