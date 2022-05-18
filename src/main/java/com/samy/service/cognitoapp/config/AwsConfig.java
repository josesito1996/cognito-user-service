package com.samy.service.cognitoapp.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderAsyncClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.samy.service.cognitoapp.repository")
@Slf4j
public class AwsConfig {

	@Value(value = "${aws.default.region}")
	private String region;

	@Value(value = "${aws.access.access-key}")
	private String accessKey;

	@Value(value = "${aws.access.secret-key}")
	private String secretKey;

	@Value("${aws.access.service-endpoint}")
	private String serviceEnpoint;

	@Value("${spring.profiles.active}")
	private String enviroment;

	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		return AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(endpointConfiguration())
				.withCredentials(awsCredentialsProvider()).build();
	}

	@Bean
	@Primary
	public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
		log.info("Enviromment {}", enviroment);
		DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB,
				new DynamoDBMapperConfig.Builder().withTableNameResolver(new TableNameResolver(enviroment)).build());
		return mapper;
	}

	@Bean
	public AWSCognitoIdentityProvider cognitoClient() {
		BasicAWSCredentials awsCred = new BasicAWSCredentials(accessKey, secretKey);
		return AWSCognitoIdentityProviderAsyncClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCred)).withRegion(region).build();
	}

	@Bean
	public AWSLambda getAwsLambda() {
		return AWSLambdaClientBuilder.standard().withCredentials(awsCredentialsProvider()).withRegion(Regions.US_EAST_2)
				.build();
	}

	public AwsClientBuilder.EndpointConfiguration endpointConfiguration() {
		return new AwsClientBuilder.EndpointConfiguration(serviceEnpoint, region);
	}

	public AWSCredentialsProvider awsCredentialsProvider() {
		return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
	}

}
