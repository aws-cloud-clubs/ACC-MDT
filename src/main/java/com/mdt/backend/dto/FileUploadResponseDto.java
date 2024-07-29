package com.mdt.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "AWS S3 업로드용 PresignedUrl 응답")
public class FileUploadResponseDto {

  private String presignedUrl;
}
