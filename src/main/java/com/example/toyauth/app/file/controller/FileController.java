package com.example.toyauth.app.file.controller;

import com.example.toyauth.app.file.domain.dto.AttachmentFileDto;
import com.example.toyauth.app.file.service.FileService;
import com.nimbusds.jose.util.Resource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/files")
public class FileController {

  private final FileService fileService;

  @Operation(
      summary = "파일 업로드",
      description = "파일을 업로드하고 파일 정보를 반환합니다."
  )
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "파일 업로드 성공",
          content = @Content(schema = @Schema(implementation = AttachmentFileDto.class))
      ),
      @ApiResponse(responseCode = "400", description = "잘못된 요청"),
      @ApiResponse(responseCode = "413", description = "파일 크기 초과"),
      @ApiResponse(responseCode = "415", description = "지원하지 않는 파일 형식")
  })
  @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<AttachmentFileDto> uploadFile(
      @Parameter(description = "업로드할 파일", required = true)
      @RequestParam("file") MultipartFile file,

      @Parameter(description = "파일 코드 (PRODUCT_IMAGE, PROFILE_IMAGE 등)")
      @RequestParam(value = "fileCode", required = false) String fileCode,

      @Parameter(description = "파일 설명")
      @RequestParam(value = "fileExplain", required = false) String fileExplain
  ) {

    AttachmentFileDto uploadedFile = fileService.uploadFile(file, fileCode, fileExplain);
    return ResponseEntity.ok(uploadedFile);
  }

  @Operation(
      summary = "파일 다운로드",
      description = "파일 ID로 파일을 다운로드합니다."
  )
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "파일 다운로드 성공"),
      @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음"),
      @ApiResponse(responseCode = "500", description = "파일 다운로드 중 오류 발생")
  })
  @GetMapping("/download/{fileId}")
  public ResponseEntity<Resource> downloadFile(
      @Parameter(description = "다운로드할 파일 ID", required = true)
      @PathVariable Long fileId,
      HttpServletRequest request
  ) {
    try {
      Resource resource = fileService.loadFileAsResource(fileId);
      AttachmentFileDto fileInfo = fileService.getFileInfo(fileId);

      // 파일의 Content-Type 결정
      String contentType = null;
      try {
        contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
      } catch (IOException ex) {
        // Content-Type을 결정할 수 없는 경우 기본값 사용
      }

      if (contentType == null) {
        contentType = "application/octet-stream";
      }

      return ResponseEntity.ok()
          .contentType(MediaType.parseMediaType(contentType))
          .header(HttpHeaders.CONTENT_DISPOSITION,
              "attachment; filename=\"" + fileInfo.getOrgFileNm() + "\"")
          .body(resource);

    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }

}
