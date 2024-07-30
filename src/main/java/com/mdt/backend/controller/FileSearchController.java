package com.mdt.backend.controller;

import com.mdt.backend.dto.FileSearchRequestDto;
import com.mdt.backend.dto.FileSearchResponseDto;
import com.mdt.backend.service.DbService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "SEARCH_API", description = "해당 파일경로가 있는지 확인하기 위한 검색 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FileSearchController {

    private final DbService dbService;


    @Operation(summary = "파일 검색", description = "파일 경로를 검색합니다. 파일 경로가 없으면 전체 파일 경로를 반환, "
        + "파일 경로에 대한 입력을 하였을 경우 유사한 파일 경로에 대한 결과물을 제공합니다.")
    @Parameter(name = "filePath", description = "파일 경로에 관한 정보를 입력. 입력하지 않으면 전체 파일 경로를 반환")
    @GetMapping("/search")
    public ResponseEntity<FileSearchResponseDto> searchFiles(@RequestBody(required = false) FileSearchRequestDto fileSearchRequestDto) {
        List<String> searchedFiles = dbService.searchFiles(fileSearchRequestDto);
        return ResponseEntity.ok(new FileSearchResponseDto("파일 검색 성공", searchedFiles));
    }


}
