package com.mdt.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "AWS S3 업로드용 PresignedUrl 요청")
public class FileUploadRequestDto {

  private String userId;
  private String fileName;
}
