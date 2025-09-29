package com.example.toyauth.app.common.exception;

import lombok.Getter;

@Getter
public class FileException extends RuntimeException {
  private final ErrorCode errorCode;
  private final String detail;

  // 1. 기본 생성자
  public FileException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.detail = null;
  }

  // 2. 상세 정보 포함 생성자 ⭐
  public FileException(ErrorCode errorCode, String detail) {
    super(errorCode.getMessage() + (detail != null ? " - " + detail : ""));
    this.errorCode = errorCode;
    this.detail = detail;
  }
}