package com.example.toyauth.app.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component("s3FileStorageService")
@ConditionalOnProperty(name = "storage.type", havingValue = "s3")
@RequiredArgsConstructor
public class S3FileStorageService {

  /*private final S3Client s3Client;

  @Value("${aws.s3.bucket}")
  private String bucketName;

  @Value("${aws.s3.region:ap-northeast-2}")
  private String region;

  @Override
  public String store(MultipartFile file, String storeFileName) {
    try {
      String key = generateS3Key(storeFileName);

      // S3에 파일 업로드
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .contentType(file.getContentType())
          .contentLength(file.getSize())
          .build();

      s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

      // S3 URL 생성
      String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);

      log.info("S3 파일 업로드 성공: {}", key);
      return fileUrl;

    } catch (IOException e) {
      log.error("S3 파일 업로드 실패: {}", storeFileName, e);
      throw new RuntimeException("S3 파일 저장에 실패했습니다.", e);
    }
  }

  @Override
  public Resource load(String storeFileName) {
    try {
      String key = generateS3Key(storeFileName);

      // S3에서 파일 존재 확인
      HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .build();

      s3Client.headObject(headObjectRequest);

      // Pre-signed URL 생성하여 Resource 반환
      String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
      return new UrlResource(fileUrl);

    } catch (NoSuchKeyException e) {
      throw new RuntimeException("S3에서 파일을 찾을 수 없습니다: " + storeFileName);
    } catch (MalformedURLException e) {
      throw new RuntimeException("S3 파일 URL이 잘못되었습니다: " + storeFileName, e);
    }
  }

  @Override
  public void delete(String storeFileName) {
    try {
      String key = generateS3Key(storeFileName);

      DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
          .bucket(bucketName)
          .key(key)
          .build();

      s3Client.deleteObject(deleteObjectRequest);

      log.info("S3 파일 삭제 성공: {}", key);

    } catch (Exception e) {
      log.error("S3 파일 삭제 실패: {}", storeFileName, e);
      throw new RuntimeException("S3 파일 삭제에 실패했습니다.", e);
    }
  }

  *//**
   * S3 Key 생성 (폴더 구조 포함)
   *//*
  private String generateS3Key(String storeFileName) {
    String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    return String.format("uploads/%s/%s", dateFolder, storeFileName);
  }*/
}
