package com.samy.service.cognitoapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordResult;
import com.samy.service.cognitoapp.model.request.UserRequestBody;
import com.samy.service.cognitoapp.model.response.UserResponseBody;
import com.samy.service.cognitoapp.service.CognitoService;
import com.samy.service.cognitoapp.service.UsuarioService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CognitoServiceImpl implements CognitoService {

	@Autowired
	private CognitoRequestBuilder builder;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private AWSCognitoIdentityProvider cognitoClient;

	@Override
	public UserResponseBody registrarUsuario(UserRequestBody body) {
		AdminSetUserPasswordResult result = builder.build(body, cognitoClient);
		if (result.getSdkResponseMetadata() != null) {
			log.info("Dentro " + result);
			return new UserResponseBody(usuarioService.registrarUsuario(body).getIdUsuario());
		}
		log.error("Error " + result);
		return new UserResponseBody("Error al registrar");
	}

}
