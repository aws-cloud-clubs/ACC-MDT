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
    /*
    @PostConstruct // 테스트를 위한 데이터 2개를 넣었습니다! -> 계속 덮어쓰기로 진행
    public void putTestDate(){

        for(int i=0; i<6000; i++){  // 1번
            String userId="user"+i;
            String fileName=i+".txt";
            int fileSize=1024;
            String fileType="txt";
            int fileContentLength=100;
            String createdAt="2024.07.28";
            fileInfoRepository.save(FileInfo.builder().
                userId(userId).
                fileName(fileName).
                fileSize(fileSize).
                fileType(fileType).
                fileContentLength(fileContentLength).
                createdAt(createdAt).
                build());
        }


        for(int i=0; i<6000; i++){ // 2번
            String userId="user"+i;
            String fileName="index.html";
            int fileSize=1024;
            String fileType="txt";
            int fileContentLength=100;
            String createdAt="2024.07.28";
            fileInfoRepository.save(FileInfo.builder().
                userId(userId).
                fileName(fileName).
                fileSize(fileSize).
                fileType(fileType).
                fileContentLength(fileContentLength).
                createdAt(createdAt).
                build());
        }
    }
    */



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
