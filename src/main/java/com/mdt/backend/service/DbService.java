package com.mdt.backend.service;

import com.mdt.backend.dto.FileSearchRequestDto;
import java.util.List;

public interface DbService {

    List<String> searchFiles(FileSearchRequestDto filePath);

    void uploadFile(String filePath);

}
