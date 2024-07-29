package com.mdt.backend.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.mdt.backend.dto.FileUploadResponseDto;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileService {

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucket;

  private final AmazonS3 amazonS3;

  public FileUploadResponseDto getPresignedUrlToUpload(String userId, String fileName) {
    GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
        bucket, createPath(userId, fileName))
        .withMethod(HttpMethod.PUT)
        .withExpiration(getPresignedUrlExpiration());

    generatePresignedUrlRequest.addRequestParameter(
        Headers.S3_CANNED_ACL,
        CannedAccessControlList.PublicRead.toString()
    );

    return FileUploadResponseDto.builder()
        .presignedUrl(amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString())
        .build();
  }

  private Date getPresignedUrlExpiration() {
    Date expiration = new Date();
    long expTimeMillis = expiration.getTime();
    expTimeMillis += 1000 * 60 * 2; //2분 설정
    expiration.setTime(expTimeMillis);

    return expiration;
  }

  private String createPath(String userId, String fileName) {
    return String.format("%s/%s", userId, fileName);
  }
}
