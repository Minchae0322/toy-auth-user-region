package com.example.toyauth.app.file.controller;

import static com.example.toyauth.app.common.util.FileUtil.determineContentType;
import static com.example.toyauth.app.common.util.FileUtil.encodeFileName;

import com.example.toyauth.app.common.enumuration.FileCode;
import com.example.toyauth.app.file.domain.dto.AttachmentFileDto;
import com.example.toyauth.app.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "FileController", description = "첨부파일 등록")
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

  private final FileService fileService;

  @Operation(summary = "파일 업로드", description = "파일을 업로드하고 파일 정보를 반환합니다.")
  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<AttachmentFileDto> uploadFile(
      @Parameter(description = "업로드할 파일", required = true)
      @RequestParam("file") MultipartFile file,

      @Parameter(description = "파일 코드 (PRODUCT_IMAGE, PROFILE_IMAGE 등)")
      @RequestParam(value = "fileCode", required = false) FileCode fileCode,

      @Parameter(description = "파일 설명")
      @RequestParam(value = "fileExplain", required = false) String fileExplain
  ) {

    AttachmentFileDto uploadedFile = fileService.uploadFile(file, fileCode, fileExplain);
    return ResponseEntity.ok(uploadedFile);
  }

  @Operation(summary = "파일 다운로드", description = "파일 ID로 파일을 다운로드합니다.")
  @GetMapping("/download/{fileId}")
  public ResponseEntity<Resource> downloadFile(
      @Parameter(description = "다운로드할 파일 ID", required = true)
      @PathVariable Long fileId,
      HttpServletRequest request
  ) {
    // Service에서 예외 발생 시 GlobalExceptionHandler가 처리
    Resource resource = fileService.loadFileAsResource(fileId);
    AttachmentFileDto fileInfo = fileService.getFileInfo(fileId);

    // Content-Type 결정
    String contentType = determineContentType(resource, request);

    // 파일명 인코딩 (한글 파일명 지원)
    String encodedFileName = encodeFileName(fileInfo.getOrgFileNm());

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + encodedFileName + "\"")
        .body(resource);
  }


}
