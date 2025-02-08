package com.example.toyauth.app.common.handler;

import com.example.toyauth.app.common.dto.JwtDto;
import com.example.toyauth.app.common.util.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.example.toyauth.app.common.util.JwtProvider.REFRESH_TOKEN_EXPIRATION;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@Getter
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        JwtDto jwtDto = jwtProvider.generateAccessAndRefreshTokens(authentication);

        String accessToken = jwtDto.getAccessToken();
        String refreshToken = jwtDto.getRefreshToken();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(REFRESH_TOKEN_EXPIRATION) // 7일 유지
                .build();

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());


        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("accessToken", accessToken);

        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
        response.getWriter().flush();
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
