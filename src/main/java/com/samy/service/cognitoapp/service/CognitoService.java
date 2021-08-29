package com.samy.service.cognitoapp.service;

import com.samy.service.cognitoapp.model.request.UserRequestBody;
import com.samy.service.cognitoapp.model.response.UserResponseBody;

public interface CognitoService {

	public UserResponseBody registrarUsuario(UserRequestBody body);
	
	public UserResponseBody registrarUsuarioV2(UserRequestBody body);
	
	public UserResponseBody eliminarUsuario(String userName);

}
