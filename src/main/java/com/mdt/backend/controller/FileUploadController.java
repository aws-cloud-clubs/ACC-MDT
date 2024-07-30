package com.mdt.backend.controller;

import com.mdt.backend.dto.FileUploadRequestDto;
import com.mdt.backend.dto.FileUploadResponseDto;
import com.mdt.backend.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "UPLOAD_API", description = "파일을 업로드 하기위한 PresignedUrl 발급용 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FileUploadController {

  private final FileService fileService;

  @Operation(summary = "파일 업로드", description = "Upload용 Presigned URL 생성")
  @PostMapping("/file/upload")
  public ResponseEntity<FileUploadResponseDto> getPresignedUrlToUpload(
      @RequestBody(required = false) FileUploadRequestDto fileUploadRequestDto) {
    FileUploadResponseDto fileUploadResponseDto = fileService.getPresignedUrlToUpload(
        fileUploadRequestDto.getUserId(), fileUploadRequestDto.getFileName());
    return ResponseEntity.ok(fileUploadResponseDto);
  }
}
