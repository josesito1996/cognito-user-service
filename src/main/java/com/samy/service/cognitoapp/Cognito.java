package com.samy.service.cognitoapp;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CognitoIdentityProviderException;

public class Cognito {
	public static void createNewUser(CognitoIdentityProviderClient cognitoclient, String userPoolId, String name,
			String email, String password) {

		try {

			AttributeType userAttrs = AttributeType.builder().name("email").value(email).build();

			AdminCreateUserRequest userRequest = AdminCreateUserRequest.builder().userPoolId(userPoolId).username(name)
					.temporaryPassword(password).userAttributes(userAttrs).messageAction("SUPPRESS").build();

			AdminCreateUserResponse response = cognitoclient.adminCreateUser(userRequest);
			System.out.println(
					"User " + response.user().username() + "is created. Status: " + response.user().userStatus());

		} catch (CognitoIdentityProviderException e) {
			System.err.println(e.awsErrorDetails().errorMessage());
			System.exit(1);
		}
	}
}
