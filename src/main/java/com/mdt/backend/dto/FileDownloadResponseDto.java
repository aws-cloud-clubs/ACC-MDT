package com.mdt.backend.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileDownloadResponseDto {

    private String message;
    private String downloadUrl;


}
