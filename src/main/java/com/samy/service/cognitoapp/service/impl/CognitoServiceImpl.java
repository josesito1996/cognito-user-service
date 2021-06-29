package com.samy.service.cognitoapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserRequest;
import com.amazonaws.services.cognitoidp.model.AdminCreateUserResult;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordResult;
import com.samy.service.cognitoapp.model.request.UserRequestBody;
import com.samy.service.cognitoapp.model.response.UserResponseBody;
import com.samy.service.cognitoapp.service.CognitoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CognitoServiceImpl implements CognitoService {

	@Autowired
	private CognitoRequestBuilder builder;

	@Autowired
	private AWSCognitoIdentityProvider cognitoClient;

	public AdminCreateUserResult createUserResult(AdminCreateUserRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserResponseBody registrarUsuario(UserRequestBody body) {
		AdminSetUserPasswordResult result = builder.build(body, cognitoClient);
		if (result.getSdkResponseMetadata() != null) {
			log.info("Dentro " + result);
			return new UserResponseBody(result.getSdkResponseMetadata().getRequestId());
		}
		log.error("Error " + result);
		return new UserResponseBody("Error al registrar");
	}

}
