package com.example.toyauth.app.file.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

  /**
   * 파일 저장
   * @param file 저장할 파일
   * @param storeFileName 저장될 파일명
   * @return 파일 접근 URL
   */
  String store(MultipartFile file, String storeFileName);

  /**
   * 파일 로드
   * @param storeFileName 저장된 파일명
   * @return 파일 리소스
   */
  Resource load(String storeFileName);

  /**
   * 파일 삭제
   * @param storeFileName 삭제할 파일명
   */
  void delete(String storeFileName);
}
