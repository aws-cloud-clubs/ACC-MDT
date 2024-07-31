package com.mdt.backend.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.mdt.backend.dto.FileUploadResponseDto;
import com.mdt.backend.exception.FileGetException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3 amazonS3;

  @Value("${aws.s3.bucket.name}")
  private String bucketName;

  public FileUploadResponseDto getPresignedUrlToUpload(String userId, String fileName) {
    GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
        bucketName, createPath(userId, fileName))
        .withMethod(HttpMethod.PUT)
        .withExpiration(getPresignedUrlExpiration());

    generatePresignedUrlRequest.addRequestParameter(
        Headers.S3_CANNED_ACL,
        CannedAccessControlList.PublicRead.toString()
    );

    return FileUploadResponseDto.builder()
        .message("파일 업로드 Url 발급 성공")
        .presignedUrl(amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString())
        .build();
  }


  public String generatePresignedUrl(String filePath, boolean forDownload) {
    if (filePath == null || filePath.isEmpty()) {
      throw new FileGetException("File path cannot be null or empty");
    }

    if (bucketName == null || bucketName.isEmpty()) {
      throw new FileGetException("Bucket name cannot be null or empty");
    }

    try {
      // 버킷이 존재하는지 확인
      if (!amazonS3.doesBucketExistV2(bucketName)) {
        throw new FileGetException(bucketName + "버킷이 존재하지 않습니다.");
      }

      // 파일 존재하는지 확인
      amazonS3.getObjectMetadata(bucketName, filePath);

      GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
          bucketName, filePath)
          .withMethod(HttpMethod.GET)
          .withExpiration(getPresignedUrlExpiration());

      //다운로드 요청일 때는 Content-Disposition 헤더 추가
      if (forDownload) {
        // 한글 파일명 처리할 수 있도록 파일명을 UTF-8로 인코딩
        String encodedFileName = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
        generatePresignedUrlRequest.addRequestParameter("response-content-disposition",
                "attachment; filename=\"" + encodedFileName + "\"");
      }

      return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    } catch (AmazonS3Exception e) {
      if (e.getStatusCode() == 404) {
        throw new FileGetException(filePath + "파일이 존재하지 않습니다.");
      }
      throw new FileGetException("presignedUrl 생성 실패" + e.getMessage());
    } catch (Exception e) {
      throw new FileGetException("presignedUrl 생성 실패 " + e.getMessage());
    }
  }

  private Date getPresignedUrlExpiration() {
    Date expiration = new Date();
    long expTimeMillis = expiration.getTime();
    expTimeMillis += 1000 * 60 * 10; //10분 설정
    expiration.setTime(expTimeMillis);

    return expiration;
  }

  private String createPath(String userId, String fileName) {
    return String.format("%s/%s", userId, fileName);
  }
}

