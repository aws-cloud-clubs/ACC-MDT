package com.mdt.backend.repository;

import com.mdt.backend.domain.FileInfo;
import java.util.List;

public interface FileInfoRepository {

    void save(FileInfo fileInfo);

    List<FileInfo> findByQueryFilePath(String userName, String fileName);

    List<FileInfo> findAll(String userName);

}
