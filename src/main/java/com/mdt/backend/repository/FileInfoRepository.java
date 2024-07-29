package com.mdt.backend.repository;

import com.mdt.backend.domain.FileInfo;
import java.util.List;

public interface FileInfoRepository {


    void save(FileInfo fileInfo);

    List<FileInfo> findByFilePath(String filePath);

    void deleteByFilePath(String filePath);

    List<FileInfo> findAll();

}
