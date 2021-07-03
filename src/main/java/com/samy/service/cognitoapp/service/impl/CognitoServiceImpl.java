package com.samy.service.cognitoapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserResult;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordResult;
import com.samy.service.cognitoapp.exception.FoundException;
import com.samy.service.cognitoapp.model.Usuario;
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
		Usuario usuarioEncontrado = usuarioService.buscarPorUserName(body.getNombreUsuario());
		if (usuarioEncontrado != null) {
			throw new FoundException("Usuario " + body.getNombreUsuario() + " ya existe en la base de datos");
		}
		AdminSetUserPasswordResult result = builder.addUser(body, cognitoClient);
		if (result.getSdkResponseMetadata() != null) {
			log.info("Dentro " + result);
			Usuario userUsuario = usuarioService.registrarUsuario(body);
			return new UserResponseBody(userUsuario.getIdUsuario(), userUsuario.getNombres(), userUsuario.getCorreo());
		}
		log.error("Error " + result);
		return new UserResponseBody("Error al registrar", "", "");
	}

	@Override
	public UserResponseBody eliminarUsuario(String userName) {
		Usuario usuario = usuarioService.buscarPorNombreUsuario(userName);
		AdminDeleteUserResult deleteResult = builder.deleteUser(userName, cognitoClient);
		if (deleteResult.getSdkResponseMetadata().getRequestId() != null) {

			usuario.setEstado(false);
			return new UserResponseBody(usuarioService.modificar(usuario).getIdUsuario(), "", "");
		}
		return new UserResponseBody("Error al Eliminar", "", "");
	}

}
