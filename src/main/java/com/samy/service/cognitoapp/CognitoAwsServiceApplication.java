package com.samy.service.cognitoapp;

import static com.samy.service.cognitoapp.Cognito.createNewUser;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;

@SpringBootApplication
public class CognitoAwsServiceApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(CognitoAwsServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		CognitoIdentityProviderClient cognitoclient = CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_2)
                .build();
		createNewUser(cognitoclient, "us-east-2_UtujH4zQk", "papitoxd", "castillochalquejose@outlook.es", "51st3ma$2021.XDD");
		
	}

}
