package com.mdt.backend.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import java.util.Map;

public class DynamoDbExpressionProvider {


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
