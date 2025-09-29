package com.example.toyauth.app.common.exception.impl;

import com.example.toyauth.app.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileErrorCode implements ErrorCode {

  // 400 Bad Request
  INVALID_FILE_EMPTY(HttpStatus.BAD_REQUEST, "File is empty"),
  INVALID_FILE_NAME(HttpStatus.BAD_REQUEST, "Invalid file name"),
  INVALID_FILE_PATH(HttpStatus.BAD_REQUEST, "Invalid file path detected"),

  // 404 Not Found
  FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "File not found"),

  // 413 Payload Too Large
  FILE_SIZE_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "File size exceeded maximum limit"),

  // 415 Unsupported Media Type
  UNSUPPORTED_FILE_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported file type"),
  INVALID_FILE_EXTENSION(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Invalid file extension"),

  // 500 Internal Server Error
  FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file"),
  FILE_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to download file"),
  FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete file"),
  FILE_STORAGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "File storage error occurred"),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}