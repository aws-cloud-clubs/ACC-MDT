package com.mdt.backend.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.mdt.backend.domain.FileInfo;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DynamoDbExpressionProvider {

    private static final String PARTITION_KEY=":user_id";
    private static final String KEY_CONDITION = "user_id = "+ PARTITION_KEY;


    public static DynamoDBQueryExpression<FileInfo> provideQueryExpression(String userId, String fileName) {
        Map<String, AttributeValue> attributeMap = new HashMap<>();

        attributeMap.put(PARTITION_KEY, new AttributeValue(userId));
        attributeMap.put(":file_name", new AttributeValue().withS(fileName));
        return new DynamoDBQueryExpression<FileInfo>()
            .withKeyConditionExpression(KEY_CONDITION)
            .withIndexName("created_at-index")
            .withFilterExpression("contains(file_name,:file_name)")
            .withExpressionAttributeValues(attributeMap);
    }

    public static DynamoDBQueryExpression<FileInfo> provideEmptyQueryExpression(String userId) {
        Map<String, AttributeValue> attributeMap = new HashMap<>();

        attributeMap.put(PARTITION_KEY, new AttributeValue(userId));

        return new DynamoDBQueryExpression<FileInfo>()
            .withKeyConditionExpression(KEY_CONDITION)
            .withExpressionAttributeValues(attributeMap);

    }


}
