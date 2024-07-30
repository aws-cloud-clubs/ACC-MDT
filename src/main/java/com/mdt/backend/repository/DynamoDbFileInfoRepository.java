package com.mdt.backend.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.mdt.backend.domain.FileInfo;
import com.mdt.backend.exception.FileSearchException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class DynamoDbFileInfoRepository implements FileInfoRepository {

    private final AmazonDynamoDBClient client;

    @Override
    public void save(FileInfo fileInfo) {
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(client);
        dynamoDBMapper.save(fileInfo);
    }

    @Override
    public List<FileInfo> findByFilePath(String filePath) {
        try {
            DynamoDBMapper mapper = new DynamoDBMapper(client);

            DynamoDBScanExpression expression = DynamoDbExpressionProvider.
                provideFilterScanExpression(
                ":file_path", filePath,
                "contains(file_path, :file_path)");

            PaginatedScanList<FileInfo> scan = mapper.scan(FileInfo.class,
                expression);

            return scan.stream().toList();
        } catch (RuntimeException e) {
            log.error("error : {}", e.getMessage());
            throw new FileSearchException();
        }
    }

    @Override
    public void deleteByFilePath(String filePath) {

    }

    @Override
    public List<FileInfo> findAll() {
        try {
            DynamoDBMapper mapper = new DynamoDBMapper(client);
            PaginatedScanList<FileInfo> scan = mapper.scan(FileInfo.class,
                DynamoDbExpressionProvider.provideEmptyScanExpression());
            return scan.stream().toList();
        } catch (RuntimeException e) {
            log.error("error : {}", e.getMessage());
            throw new FileSearchException();
        }
    }
}
