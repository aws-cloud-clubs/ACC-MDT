package com.mdt.backend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileSearchResponseDto {
    private int status;
    private String message;
    private List<String> fileNames;
}
