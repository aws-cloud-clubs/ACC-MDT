package com.mdt.backend.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsConfig {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonDynamoDBClient dynamoDBClient() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonDynamoDBClient) AmazonDynamoDBClient.builder()
            .withRegion(region)
            .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
            .build();
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        DynamoDBMapperConfig builder = DynamoDBMapperConfig.builder().
            withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.CLOBBER) // update 시 덮어쓰기
            .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT) // 강력한 일관성 적용
            .withTableNameOverride(null) // 테이블 이름 재정의 x
            .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING) //  페이지네이션을 통해서 가져오기보다 모든 내용을 다 가져오는 방식
            .build();

        return new DynamoDBMapper(dynamoDBClient(), builder);
    }

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();
    }
}
