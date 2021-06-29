package com.samy.service.cognitoapp.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samy.service.cognitoapp.model.Usuario;
import com.samy.service.cognitoapp.model.request.UserRequestBody;
import com.samy.service.cognitoapp.repository.GenericRepo;
import com.samy.service.cognitoapp.repository.UsuarioRepo;
import com.samy.service.cognitoapp.service.UsuarioService;

@Service
public class UsuarioServiceImpl extends CrudImpl<Usuario, String> implements UsuarioService {

	@Autowired
	private UsuarioRepo repo;

	@Autowired
	private UserRequestBuilder builder;

	@Override
	protected GenericRepo<Usuario, String> getRepo() {
		return repo;
	}

	@Override
	public Usuario registrarUsuario(UserRequestBody body) {
		Usuario usuario = builder.transformFromUserRequestBody(body);
		usuario.setFechaCreacion(LocalDateTime.now());
		usuario.setEstado(true);
		return registrar(usuario);
	}

}
