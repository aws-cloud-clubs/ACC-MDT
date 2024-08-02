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



    // 테스트를 위해 한 번 12000개의 임시 데이터를 넣었습니다.
    //  1번 케이스 -> 유저 이름 파일 이름 둘 다 다른 경우
    // 2번 케이스 -> 유저 이름 다르지만 파일 이름이 동일한 경우


    @Override
    public List<String> searchFiles(FileSearchRequestDto dto) {
        if (isRequestFileNameEmpty(dto))
            return fileInfoRepository.findAll(dto.getUserId()).stream().map(fileInfo  -> fileInfo.getUserId() + "/" +  fileInfo.getFileName()).toList();

        return fileInfoRepository.findByQueryFilePath(dto.getUserId(), dto.getFileName()).stream()
            .map(fileInfo -> fileInfo.getUserId() + "/" + fileInfo.getFileName()).toList();
    }

    private boolean isRequestFileNameEmpty(FileSearchRequestDto dto) {
        String fileName = dto.getFileName();
        return (Objects.isNull(fileName) || fileName.isEmpty());
    }

}
