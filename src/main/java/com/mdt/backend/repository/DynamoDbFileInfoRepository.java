package com.mdt.backend.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
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
    public List<FileInfo> findByQueryFilePath(String userId, String fileName) {
        try {
            DynamoDBMapper mapper = new DynamoDBMapper(client);

            DynamoDBQueryExpression<FileInfo> expr = DynamoDbExpressionProvider.
                provideQueryExpression(userId,fileName);

            PaginatedQueryList<FileInfo> query = mapper.query(FileInfo.class, expr);

            return query.stream().toList();
        } catch (RuntimeException e) {
            log.error("error : {}", e.getMessage());
            throw new FileSearchException();
        }
    }


    @Override
    public List<FileInfo> findAll(String userId) {
        try {
            DynamoDBMapper mapper = new DynamoDBMapper(client);
            PaginatedQueryList<FileInfo> query = mapper.query(FileInfo.class,
                DynamoDbExpressionProvider.provideEmptyQueryExpression(userId));
            return query.stream().toList();
        } catch (RuntimeException e) {
            log.error("error : {}", e.getMessage());
            throw new FileSearchException();
        }
    }
}
