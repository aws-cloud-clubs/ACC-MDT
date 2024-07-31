package com.mdt.backend.service;

import com.mdt.backend.dto.FileSearchRequestDto;
import com.mdt.backend.repository.FileInfoRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AwsDynamoDbService implements DbService {

    private final FileInfoRepository fileInfoRepository;

    @Override
    public List<String> searchFiles(FileSearchRequestDto dto) {
        if (isRequestFilePathEmpty(dto))
            return fileInfoRepository.findAll().stream().map(fileInfo  -> fileInfo.getUserId() + "/" + fileInfo.getFileName()).toList();

        return fileInfoRepository.findByQueryFilePath(dto.getFileName()).stream()
            .map(fileInfo -> fileInfo.getUserId() + "/" + fileInfo.getFileName()).toList();
    }

    private boolean isRequestFilePathEmpty(FileSearchRequestDto dto) {
        String fileName = dto.getFileName();
        return (Objects.isNull(fileName) || fileName.isEmpty());
    }

}
