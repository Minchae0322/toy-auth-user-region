package com.example.toyauth.app.file.service;

import com.example.toyauth.app.common.enumuration.FileCode;
import com.example.toyauth.app.common.enumuration.StorageType;
import com.example.toyauth.app.common.exception.FileException;
import com.example.toyauth.app.common.exception.impl.FileErrorCode;
import com.example.toyauth.app.file.domain.AttachmentFile;
import com.example.toyauth.app.file.domain.dto.AttachmentFileDto;
import com.example.toyauth.app.file.repository.AttachmentFileRepository;
import org.springframework.core.io.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

  @Qualifier(value = "localFileStorageService")
  private final FileStorageService fileStorageService;
  private final AttachmentFileRepository attachmentFileRepository;

  @Value("${file.max-size:10485760}")  // 10MB
  private long maxFileSize;

  @Value("${file.allowed-extensions:jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx}")
  private String allowedExtensions;

  /**
   * 파일 업로드
   */
  @Transactional
  public AttachmentFileDto uploadFile(MultipartFile file, FileCode fileCode, String fileExplain) {
    validateFile(file);
    String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
    String storeFileName = generateStoreFileName(originalFilename);

    try {
      // 스토리지에 파일 저장
      String fileUrl = fileStorageService.store(file, storeFileName);

      // DB에 파일 메타데이터 저장
      AttachmentFile attachmentFile = AttachmentFile.builder()
          .orgFileNm(originalFilename)
          .storeFileNm(storeFileName)
          .fileUrl(fileUrl)
          .fileSize(file.getSize())
          .fileExt(getFileExtension(originalFilename))
          .fileCode(fileCode)
          .fileExplain(fileExplain)
          .storageType(StorageType.LOCAL)
          .build();

      AttachmentFile savedFile = attachmentFileRepository.save(attachmentFile);
      log.info("파일 업로드 성공 - ID: {}, 파일명: {}", savedFile.getId(), originalFilename);
      return AttachmentFileDto.from(savedFile);

    } catch (Exception e) {
      log.error("파일 업로드 실패: {}", originalFilename, e);
      throw new FileException(FileErrorCode.FILE_UPLOAD_FAILED, originalFilename);
    }

  }

  /**
   * 파일 다운로드를 위한 리소스 로드
   */
  @Transactional(readOnly = true)
  public Resource loadFileAsResource(Long fileId) {
    AttachmentFile file = attachmentFileRepository.findById(fileId)
        .orElseThrow(() -> new FileException(FileErrorCode.FILE_NOT_FOUND, "ID: " + fileId));

    try {
      return fileStorageService.load(file.getStoreFileNm());
    } catch (Exception e) {
      log.error("파일 로드 실패 - ID: {}, 파일명: {}", fileId, file.getStoreFileNm(), e);
      throw new FileException(FileErrorCode.FILE_DOWNLOAD_FAILED, file.getOrgFileNm());
    }
  }

  /**
   * 파일 정보 조회
   */
  @Transactional(readOnly = true)
  public AttachmentFileDto getFileInfo(Long fileId) {
    AttachmentFile file = attachmentFileRepository.findById(fileId)
        .orElseThrow(() -> new FileException(FileErrorCode.FILE_NOT_FOUND, "ID: " + fileId));

    return AttachmentFileDto.from(file);
  }

  /**
   * 파일 삭제
   */
  @Transactional
  public void deleteFile(Long fileId) {
    AttachmentFile file = attachmentFileRepository.findById(fileId)
        .orElseThrow(() -> new FileException(FileErrorCode.FILE_NOT_FOUND, "ID: " + fileId));

    try {
      fileStorageService.delete(file.getStoreFileNm());
      attachmentFileRepository.delete(file);
      log.info("파일 삭제 성공 - ID: {}, 파일명: {}", fileId, file.getOrgFileNm());
    } catch (Exception e) {
      throw new FileException(FileErrorCode.FILE_DELETE_FAILED, "ID: " + fileId);
    }
  }

  /**
   * 파일 검증
   */
  private void validateFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      throw new FileException(FileErrorCode.INVALID_FILE_EMPTY);
    }

    if (file.getSize() > maxFileSize) {
      throw new FileException(FileErrorCode.FILE_SIZE_EXCEEDED,
          String.format("Maximum: %dMB", maxFileSize / 1024 / 1024));
    }

    String filename = file.getOriginalFilename();
    String extension = getFileExtension(filename);

    List<String> allowedExtList = Arrays.asList(allowedExtensions.split(","));

    if (!allowedExtList.contains(extension.toLowerCase())) {
      throw new FileException(FileErrorCode.INVALID_FILE_EXTENSION,
          String.format("Allowed: %s", allowedExtensions));
    }

    if (filename != null && filename.contains("..")) {
      throw new FileException(FileErrorCode.INVALID_FILE_PATH);
    }
  }

  /**
   * 저장용 파일명 생성 (UUID + 타임스탬프 + 확장자)
   */
  private String generateStoreFileName(String originalFilename) {
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    String uuid = UUID.randomUUID().toString().substring(0, 8);

    return Optional.ofNullable(originalFilename)
        .map(this::getFileExtension)
        .map(ext -> String.format("%s_%s.%s", timestamp, uuid, ext))
        .orElseThrow(() -> new FileException(FileErrorCode.INVALID_FILE_NAME));
  }

  /**
   * 파일 확장자 추출
   */
  private String getFileExtension(String filename) {
    return Optional.ofNullable(filename)
        .filter(name -> name.contains("."))
        .map(name -> name.substring(name.lastIndexOf(".") + 1))
        .orElse("");
  }
}
