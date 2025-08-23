package com.example.toyauth.app.common.handler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.toyauth.app.auth.domain.MyUserDetails;
import com.example.toyauth.app.auth.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

  private final RefreshTokenRepository refreshTokenRepository;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {

    if (authentication != null) {
      MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
      Long userId = userDetails.getUser().getId();

      refreshTokenRepository.deleteByUserId(userId);
    }

    clearRefreshTokenCookie(response);
    sendJsonResponse(response);
  }

  private void clearRefreshTokenCookie(HttpServletResponse response) {
    ResponseCookie expiredCookie = ResponseCookie.from("refreshToken", "")
        .httpOnly(true)
        .secure(true)
        .path("/")
        .maxAge(0)
        .build();

    response.addHeader("Set-Cookie", expiredCookie.toString());
  }

  private void sendJsonResponse(HttpServletResponse response) throws IOException {
    response.setContentType(APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpServletResponse.SC_OK);

    Map<String, Object> result = new HashMap<>();
    result.put("success", true);
    result.put("message", "로그아웃되었습니다");
    result.put("timestamp", System.currentTimeMillis());

    response.getWriter().write(objectMapper.writeValueAsString(result));
  }
}
