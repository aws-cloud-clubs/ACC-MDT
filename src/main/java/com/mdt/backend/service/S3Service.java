package com.mdt.backend.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.mdt.backend.exception.FileDownloadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;


    public String generatePresignedUrl(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new FileDownloadException("File path cannot be null or empty");
        }

        if (bucketName == null || bucketName.isEmpty()) {
            throw new FileDownloadException("Bucket name cannot be null or empty");
        }

        try {
            // 버킷이 존재하는지 확인
            if (!amazonS3.doesBucketExistV2(bucketName)) {
                throw new FileDownloadException(bucketName+"버킷이 존재하지 않습니다.");
            }

            // 파일 존재하는지 확인
            amazonS3.getObjectMetadata(bucketName, filePath);

            Date expiration = new Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += 1000 * 60 * 10; // 10분 유효기간
            expiration.setTime(expTimeMillis);

            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, filePath)
                    .withMethod(HttpMethod.GET)
                    .withExpiration(expiration);

            return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                throw new FileDownloadException(filePath+"파일이 존재하지 않습니다.");
            }
            throw new FileDownloadException("presignedUrl 생성 실패" + e.getMessage());
        } catch (Exception e) {
            throw new FileDownloadException("presignedUrl 생성 실패 " + e.getMessage());
        }
    }
}

