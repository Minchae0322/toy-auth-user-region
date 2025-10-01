package com.example.toyauth.app.config;

import java.io.File;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String userDir = System.getProperty("user.dir");
    String uploadLocation = "file:" + userDir + File.separator + "uploads" + File.separator;

    registry.addResourceHandler("/uploads/**")
        .addResourceLocations(uploadLocation)
        .setCachePeriod(3600);

    System.out.println("정적 파일 경로 설정: /uploads/** -> " + uploadLocation);
  }
}
