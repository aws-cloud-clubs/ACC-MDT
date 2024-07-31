package com.mdt.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileGetResponseDto {

    private String message;
    private String downloadUrl;


}
