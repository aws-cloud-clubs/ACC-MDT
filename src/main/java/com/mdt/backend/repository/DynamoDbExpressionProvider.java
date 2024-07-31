package com.mdt.backend.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.mdt.backend.domain.FileInfo;
import java.util.Map;

public class DynamoDbExpressionProvider {

    private static final String PARTITION_KEY=":file_name";
    private static final String KEY_CONDITION = "file_name = "+ PARTITION_KEY;


    public static DynamoDBQueryExpression<FileInfo> provideQueryExpression(String value) {
        Map<String, AttributeValue> attributeMap = Map.of(PARTITION_KEY,
            new AttributeValue(value));

        return new DynamoDBQueryExpression<FileInfo>()
            .withKeyConditionExpression(KEY_CONDITION)
            .withExpressionAttributeValues(attributeMap);
    }


    public static DynamoDBScanExpression provideFilterScanExpression(String key,String value, String filterExpression) {
        Map<String, AttributeValue> attributeMap = Map.of(key,
            new AttributeValue(value));

        return new DynamoDBScanExpression()
            .withFilterExpression(filterExpression)
            .withExpressionAttributeValues(attributeMap);
    }

    public static DynamoDBScanExpression provideEmptyScanExpression() {
        return new DynamoDBScanExpression();
    }


}
