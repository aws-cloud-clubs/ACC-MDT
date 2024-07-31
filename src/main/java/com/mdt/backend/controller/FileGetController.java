package com.mdt.backend.controller;


import com.mdt.backend.dto.FileGetRequestDto;
import com.mdt.backend.dto.FileGetResponseDto;
import com.mdt.backend.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "GET_API", description = "파일을 조회할 수 있는 presignedURL을 반환하는 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FileGetController {

    private final S3Service s3Service;

    @Operation(summary = "파일 조회", description = "파일 경로를 받아 s3에서 생성한 조회용 presignedUrl을 반환합니다.")
    @PostMapping("/get")
    public FileGetResponseDto generatePresignedUrl(@RequestBody FileGetRequestDto request) {
        String presignedUrl = s3Service.generatePresignedUrl(request.getFilePath(), false);
        return new FileGetResponseDto("Presigned URL 생성 성공", presignedUrl);
    }

}
