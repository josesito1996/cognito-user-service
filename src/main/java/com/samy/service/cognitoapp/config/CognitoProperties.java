package com.samy.service.cognitoapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Configuration
@ConfigurationProperties(prefix = "aws.cognito")
@Getter
@RefreshScope
@Setter
@ToString
public class CognitoProperties {

	private String clientId;
	
	private String userPoolId;
	
}
