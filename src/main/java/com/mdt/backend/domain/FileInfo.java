package com.mdt.backend.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DynamoDBTable(tableName = "TestFile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileInfo {

    @DynamoDBHashKey
    private String filePath;

    @DynamoDBAttribute
    private int fileSize;

    @DynamoDBAttribute
    private String fileType;

    @DynamoDBAttribute
    private int fileContentLength;

    @DynamoDBRangeKey
    @DynamoDBAttribute
    private String createdAt;

}
