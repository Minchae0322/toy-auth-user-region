package com.example.toyauth.app.common.util;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.Resource;

@UtilityClass
public final class FileUtil {
  /**
   * Content-Type 결정
   */
  public static String determineContentType(Resource resource, HttpServletRequest request) {
    String contentType = null;

    try {
      String filename = resource.getFilename();
      if (filename != null) {
        contentType = request.getServletContext().getMimeType(filename);
      }
    } catch (Exception ex) {
      // Content-Type을 결정할 수 없는 경우 무시
    }

    return contentType != null ? contentType : "application/octet-stream";
  }

  /**
   * 파일명 인코딩 (한글 파일명 지원)
   */
  public static String encodeFileName(String fileName) {
    return URLEncoder.encode(fileName, StandardCharsets.UTF_8)
        .replaceAll("\\+", "%20");
  }



}
