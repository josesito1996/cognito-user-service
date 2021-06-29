package com.samy.service.cognitoapp.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AWSCognitoIdentityProviderException;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordRequest;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.DeliveryMediumType;
import com.amazonaws.services.cognitoidp.model.MessageActionType;
import com.samy.service.cognitoapp.model.request.UserRequestBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CognitoRequestBuilder {

	@Value(value = "${aws.cognito.userPoolId}")
	private String userPoolId;

	public AdminSetUserPasswordResult build(UserRequestBody request, AWSCognitoIdentityProvider cognitoClient) {
		try {
			AttributeType emailAttr = new AttributeType().withName("email").withValue(request.getCorreo());
			AttributeType emailVerifiedAttr = new AttributeType().withName("email_verified").withValue("true");
			AdminCreateUserRequest newRequest = new AdminCreateUserRequest().withUserPoolId(userPoolId)
					.withUsername(request.getNombreUsuario()).withTemporaryPassword(request.getContraseña())
					.withUserAttributes(emailAttr, emailVerifiedAttr).withMessageAction(MessageActionType.SUPPRESS)
					.withDesiredDeliveryMediums(DeliveryMediumType.EMAIL);
			@SuppressWarnings("unused")
			AdminCreateUserResult createResult = cognitoClient.adminCreateUser(newRequest);
			AdminSetUserPasswordRequest adminSetUserPasswordRequest = new AdminSetUserPasswordRequest()
					.withUsername(request.getNombreUsuario()).withUserPoolId(userPoolId)
					.withPassword(request.getContraseña()).withPermanent(true);
			return cognitoClient.adminSetUserPassword(adminSetUserPasswordRequest);
		} catch (AWSCognitoIdentityProviderException e) {
			log.error("Error : " + e.getMessage());
			throw new AWSCognitoIdentityProviderException(e.getMessage());
		} catch (Exception ex) {
			log.error("Exception : + " + ex.getMessage());
			throw new AWSCognitoIdentityProviderException(ex.getMessage());
		}
	}

}
