package com.samy.service.cognitoapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderAsyncClientBuilder;

@Configuration
public class CognitoConfig {

	@Value(value = "${aws.default.region}")
	private String region;

	@Value(value = "${aws.access.access-key}")
	private String accessKey;

	@Value(value = "${aws.access.secret-key}")
	private String secretKey;

	@Bean
	public AWSCognitoIdentityProvider cognitoClient() {
		BasicAWSCredentials awsCred = new BasicAWSCredentials(accessKey, secretKey);
		return AWSCognitoIdentityProviderAsyncClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCred)).withRegion(region).build();
	}

}
