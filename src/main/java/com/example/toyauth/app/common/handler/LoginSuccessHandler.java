package com.example.toyauth.app.common.handler;

import com.example.toyauth.app.common.util.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;


@Getter
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        /*TokenInfo tokenInfo = jwtTokenProvider.generateAccessAndRefreshTokens(authentication);

        changeUserLoggedIn(authentication);

        TokenEdit tokenEdit = TokenEdit.builder()
                .username(authentication.getName())
                .refreshToken(tokenInfo.getRefreshToken())
                .build();

        tokenService.updateRefreshToken(tokenEdit);

        response.setStatus(HttpServletResponse.SC_OK);
       *//* response.setContentType(APPLICATION_JSON_VALUE);
        response.setHeader("Authorization", tokenInfo.getAccessToken());*//*
        redirect(request, response, tokenInfo.getAccessToken(), tokenInfo.getRefreshToken());*/
    }

    /*private void changeUserLoggedIn(Authentication authentication) {
        MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
        User user = myUserDetails.getUser();
        user.enable();

        userRepository.save(user);
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response,
                         String accessToken, String refreshToken) throws IOException {

        String uri = createURI(accessToken, refreshToken).toString();
        getRedirectStrategy().sendRedirect(request, response, uri);
    }



    private URI createURI(String accessToken, String refreshToken) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add(ACCESS_TOKEN_PARAM_NAME, accessToken);
        queryParams.add(REFRESH_TOKEN_PARAM_NAME, refreshToken);

        return UriComponentsBuilder
                .newInstance()
                .scheme(HTTP_HTTPS)
                .host(SERVER_URL)
                .port(FRONT_END_PORT_NUM)
                .path("/app")
                .queryParams(queryParams)
                .build()
                .toUri();
    }*/
}
