package com.mdt.backend.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
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

    @DynamoDBHashKey(attributeName = "user_id")
    private String userId;

    @DynamoDBRangeKey(attributeName = "file_name")
    private String fileName;

    @DynamoDBAttribute(attributeName = "file_size")
    private int fileSize;

    @DynamoDBAttribute(attributeName = "file_type")
    private String fileType;

    @DynamoDBAttribute(attributeName = "file_content_length")
    private int fileContentLength;

    @DynamoDBAttribute(attributeName = "created_at")
    @DynamoDBIndexRangeKey(localSecondaryIndexName = "created_at-index")
    private String createdAt;

}
