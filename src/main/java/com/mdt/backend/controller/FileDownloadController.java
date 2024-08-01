package com.mdt.backend.controller;


import com.mdt.backend.dto.FileDownloadRequestDto;
import com.mdt.backend.dto.FileDownloadResponseDto;
import com.mdt.backend.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "DOWNLOAD_API", description = "파일을 다운로드 할 수 있는 presignedURL을 반환하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FileDownloadController {

    private final S3Service s3Service;

    @Operation(summary = "파일 다운로드", description = "파일 경로를 받아 s3에서 생성한 다운로드용 presignedUrl을 반환합니다.")
    @PostMapping("/download")
    public FileDownloadResponseDto generatePresignedUrl(@RequestBody FileDownloadRequestDto request) {
        String presignedUrl = s3Service.generatePresignedUrl(request.getFilePath(), true);
        return new FileDownloadResponseDto("Presigned URL 생성 성공", presignedUrl);
    }

}
