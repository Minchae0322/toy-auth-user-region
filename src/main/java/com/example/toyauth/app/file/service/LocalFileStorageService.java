package com.example.toyauth.app.file.service;

import com.example.toyauth.app.common.exception.FileException;
import com.example.toyauth.app.common.exception.impl.FileErrorCode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component("localFileStorageService")
public class LocalFileStorageService implements FileStorageService {

  @Value("${file.upload.path:uploads}")
  private String uploadPath;

  @Value("${server.domain:http://localhost:8081}")
  private String serverDomain;

  @Override
  public String store(MultipartFile file, String storeFileName) {
    try {
      // 날짜 폴더 경로 생성
      String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
      Path uploadDir = createUploadPath();
      Path targetPath = uploadDir.resolve(storeFileName);

      Path baseUploadPath = Paths.get(System.getProperty("user.dir"), uploadPath).normalize();
      if (!targetPath.normalize().startsWith(baseUploadPath)) {
        throw new FileException(FileErrorCode.INVALID_FILE_PATH);
      }

      Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

      return String.format("%s/%s/%s/%s",
          serverDomain, uploadPath, dateFolder, storeFileName);

    } catch (IOException e) {
      log.error("파일 저장 실패: {}", storeFileName, e);
      throw new FileException(FileErrorCode.FILE_STORAGE_ERROR);
    }
  }

  @Override
  public Resource load(String storeFileName) {
    try {
      Path filePath = createUploadPath().resolve(storeFileName).normalize();
      Resource resource = new UrlResource(filePath.toUri());

      if (resource.exists() && resource.isReadable()) {
        return resource;
      } else {
        throw new RuntimeException("파일을 읽을 수 없습니다: " + storeFileName);
      }
    } catch (IOException e) {
      throw new RuntimeException("파일 경로가 잘못되었습니다: " + storeFileName, e);
    }
  }

  @Override
  public void delete(String storeFileName) {
    try {
      Path filePath = createUploadPath().resolve(storeFileName).normalize();
      Files.deleteIfExists(filePath);
      log.info("파일 삭제 성공: {}", storeFileName);
    } catch (IOException e) {
      log.error("파일 삭제 실패: {}", storeFileName, e);
      throw new RuntimeException("파일 삭제에 실패했습니다.", e);
    }
  }

  /**
   * 업로드 경로 생성 (user.dir 기준)
   */
  private Path createUploadPath() throws IOException {
    String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    String userDir = System.getProperty("user.dir");
    Path uploadDir = Paths.get(userDir, uploadPath, dateFolder);

    if (!Files.exists(uploadDir)) {
      Files.createDirectories(uploadDir);
    }

    return uploadDir;
  }
}
