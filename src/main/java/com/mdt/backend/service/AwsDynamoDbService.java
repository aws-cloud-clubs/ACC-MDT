package com.mdt.backend.service;

import com.mdt.backend.domain.FileInfo;
import com.mdt.backend.dto.FileSearchRequestDto;
import com.mdt.backend.repository.FileInfoRepository;
import jakarta.annotation.PostConstruct;
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


    @PostConstruct // 테스트를 위한 데이터 2개를 넣었습니다! -> 계속 덮어쓰기로 진행
    public void putTestDate(){
        fileInfoRepository.save(FileInfo.builder()
            .filePath("user1/1.txt")
            .fileSize(1024)
            .fileType("txt")
            .fileContentLength(100)
            .createdAt("2024.07.28")
            .build());

        fileInfoRepository.save(FileInfo.builder()
            .filePath("user2/2.txt")
            .fileSize(1024)
            .fileType("txt")
            .fileContentLength(100)
            .createdAt("2024.07.29")
            .build());
    }


    @Override
    public List<String> searchFiles(FileSearchRequestDto dto) {
            if(isRequestFilePathEmpty(dto))
                return fileInfoRepository.findAll().stream().map(FileInfo::getFilePath).toList();

            return fileInfoRepository.findByFilePath(dto.getFilePath()).stream().map(FileInfo::getFilePath).toList();
    }

    private boolean isRequestFilePathEmpty(FileSearchRequestDto dto) {
        return Objects.isNull(dto) || Objects.isNull(dto.getFilePath()) || dto.getFilePath()
            .isEmpty();
    }


    @Override
    public void uploadFile(String filePath) {
    }
}
