package com.samy.service.cognitoapp.service;

import com.samy.service.cognitoapp.model.Usuario;
import com.samy.service.cognitoapp.model.request.UserRequestBody;

public interface UsuarioService {

	public Usuario registrarUsuario(UserRequestBody body);
	
}
