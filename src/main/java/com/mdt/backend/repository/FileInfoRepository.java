package com.mdt.backend.repository;

import com.mdt.backend.domain.FileInfo;
import java.util.List;

public interface FileInfoRepository {


    void save(FileInfo fileInfo);

    List<FileInfo> findByScanFilePath(String fileName);


    List<FileInfo> findByQueryFilePath(String fileName);


    void deleteByFilePath(String filePath);

    List<FileInfo> findAll();

}
