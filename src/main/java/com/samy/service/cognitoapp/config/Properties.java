package com.samy.service.cognitoapp.config;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Configuration
@ConfigurationProperties(prefix = "endpoint-ag")
@Getter
@RefreshScope
@Setter
@ToString
public class Properties implements Serializable {

	private static final long serialVersionUID = -3907534184352793143L;

	private String urlUser;
	
	private String urlLogin;
	
}
