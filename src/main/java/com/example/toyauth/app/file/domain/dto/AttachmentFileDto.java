package com.example.toyauth.app.file.domain.dto;

import com.example.toyauth.app.common.enumuration.FileCode;
import com.example.toyauth.app.file.domain.AttachmentFile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttachmentFileDto {

  @Schema(description = "첨부 파일 아이디")
  private Long id;

  @Schema(description = "파일 코드")
  private FileCode fileCode;

  @Schema(description = "원본 파일 명")
  private String orgFileNm;

  @Schema(description = "저장 파일 명")
  private String storeFileNm;

  @Schema(description = "파일 url")
  private String fileUrl;

  @Schema(description = "파일 사이즈")
  private Long fileSize;

  @Schema(description = "파일 설명")
  private String fileExplain;


  public static AttachmentFileDto from(AttachmentFile attachmentFile) {
    return AttachmentFileDto.builder()
        .id(attachmentFile.getId())
        .fileCode(attachmentFile.getFileCode())
        .orgFileNm(attachmentFile.getOrgFileNm())
        .storeFileNm(attachmentFile.getStoreFileNm())
        .fileUrl(attachmentFile.getFileUrl())
        .fileSize(attachmentFile.getFileSize())
        .fileExplain(attachmentFile.getFileExplain())
        .build();
  }
}
