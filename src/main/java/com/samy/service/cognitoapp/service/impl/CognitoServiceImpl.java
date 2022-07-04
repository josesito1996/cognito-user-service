package com.samy.service.cognitoapp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminDeleteUserResult;
import com.amazonaws.services.cognitoidp.model.AdminSetUserPasswordResult;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.ChangePasswordResult;
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.InitiateAuthResult;
import com.samy.service.cognitoapp.config.CognitoProperties;
import com.samy.service.cognitoapp.exception.BadRequestException;
import com.samy.service.cognitoapp.exception.FoundException;
import com.samy.service.cognitoapp.model.ColaboradorTable;
import com.samy.service.cognitoapp.model.Usuario;
import com.samy.service.cognitoapp.model.request.ChangePasswordRequest;
import com.samy.service.cognitoapp.model.request.LoginRequest;
import com.samy.service.cognitoapp.model.request.UserRequestBody;
import com.samy.service.cognitoapp.model.response.TokenAuthResponse;
import com.samy.service.cognitoapp.model.response.UserResponseBody;
import com.samy.service.cognitoapp.service.CognitoService;
import com.samy.service.cognitoapp.service.ColaboradorService;
import com.samy.service.cognitoapp.service.UsuarioService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CognitoServiceImpl implements CognitoService {

	@Autowired
	private CognitoProperties cognitoProperties;

	@Autowired
	private CognitoRequestBuilder builder;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private ColaboradorService colaboradorService;

	@Autowired
	private AWSCognitoIdentityProvider cognitoClient;

	@Override
	public UserResponseBody registrarUsuario(UserRequestBody body) {
		Usuario usuarioEncontrado = usuarioService.buscarPorNombreUsuario(body.getNombreUsuario());
		if (usuarioEncontrado != null) {
			throw new FoundException("Usuario " + body.getNombreUsuario() + " ya existe en la base de datos");
		}
		AdminSetUserPasswordResult result = builder.addUser(body, cognitoClient);
		if (result.getSdkResponseMetadata() != null) {
			log.info("Dentro " + result);
			Usuario userUsuario = usuarioService.registrarUsuario(body);
			return UserResponseBody.builder().id(userUsuario.getIdUsuario())
					.datosUsuario(userUsuario.getNombres().concat(" ").concat(userUsuario.getApellidos()))
					.nombreUsuario(userUsuario.getNombreUsuario()).build();
		}
		log.error("Error " + result);
		throw new BadRequestException("Error al registrar usuario : " + result);
	}

	@Override
	public UserResponseBody eliminarUsuario(String userName) {
		Usuario usuario = usuarioService.buscarPorNombreUsuario(userName);
		AdminDeleteUserResult deleteResult = builder.deleteUser(userName, cognitoClient);
		if (deleteResult.getSdkResponseMetadata().getRequestId() != null) {

			usuario.setEstado(false);
			return new UserResponseBody(usuarioService.modificar(usuario).getIdUsuario(), "", "", "", usuario.getRol(),
					"", true, new ArrayList<>());
		}
		return new UserResponseBody("Error al Eliminar", "", "", "", usuario.getRol(), "", true, new ArrayList<>());
	}

	@Override
	public UserResponseBody registrarUsuarioV2(UserRequestBody body) {
		log.info("CognitoServiceImpl.registrarUsuarioV2");
		log.info("UserRequestBody : " + body.toString());

		AdminSetUserPasswordResult result = builder.addUser(body, cognitoClient);
		if (result.getSdkResponseMetadata() != null) {
			log.info("Dentro " + result);
			boolean isColaborador = body.getTipo().equals("COLABORADOR");
			if (isColaborador) {
				ColaboradorTable colabordor = colaboradorService.buscarPorCorreo(body.getCorreo());
				return UserResponseBody.builder().id(colabordor.getIdUsuario())
						.datosUsuario(colabordor.getNombres().concat(" ").concat(colabordor.getApellidos()))
						.nombreUsuario(colabordor.getCorreo()).build();
			}
			Usuario userUsuario = usuarioService.buscarPorNombreUsuarioPorCognito(body.getNombreUsuario());
			return UserResponseBody.builder().id(userUsuario.getIdUsuario())
					.datosUsuario(userUsuario.getNombres().concat(" ").concat(userUsuario.getApellidos()))
					.nombreUsuario(userUsuario.getNombreUsuario()).build();
		}
		return null;
	}

	@Override
	public UserResponseBody cambiarPasswordUsuario(ChangePasswordRequest request) {
		log.info("CognitoServiceImpl.cambiarPasswordUsuario");
		log.info("UserRequestBody : " + request);
		ChangePasswordResult result = builder.changePassword(request, cognitoClient);
		if (result.getSdkResponseMetadata() == null) {
			throw new BadRequestException("No se pudo cambiar el Password del usuario : " + request.getUserName());
		}
		ColaboradorTable colaborador = colaboradorService.buscarPorCorreo(request.getUserName());
		colaborador.setPassword(request.getNewPassword());
		colaborador.setPasswordChanged(true);
		ColaboradorTable colaboradorUpdated = colaboradorService.modificar(colaborador);
		return UserResponseBody.builder().id(colaboradorUpdated.getIdUsuario())
				.datosUsuario(colaboradorUpdated.getNombres().concat(" ").concat(colaboradorUpdated.getApellidos()))
				.nombreUsuario(colaboradorUpdated.getCorreo()).build();
	}

	@Override
	public TokenAuthResponse login(LoginRequest request) {
		Map<String, String> authParams = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;
			{
				put("USERNAME", request.getUserName());
				put("PASSWORD", request.getPassword());
			}
		};
		log.info("Properties : {}",cognitoProperties.toString());
		InitiateAuthRequest  authRequest = new InitiateAuthRequest()
				.withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
				.withClientId(cognitoProperties.getClientId())
				.withAuthParameters(authParams);
		InitiateAuthResult  authResult = cognitoClient.initiateAuth(authRequest);
		AuthenticationResultType authResultType = authResult.getAuthenticationResult();
		return TokenAuthResponse.builder()
				.token(authResultType.getIdToken())
				.data(getUserResponse(request.getUserName()))
				.build();
	}
	
	public UserResponseBody getUserResponse(String userName) {
		UserResponseBody responseUsuario = usuarioService.getUsuarioByUserName(userName);
		log.info("responseUsuario {}", responseUsuario);
		if (responseUsuario.getId() != null) {
			return responseUsuario;
		}
		UserResponseBody responseColaborador = colaboradorService.getUsuarioByUserName(userName);
		log.info("responseUsuario {}", responseUsuario);
		if (responseColaborador.getId() != null) {
			return responseColaborador;
		}
		return new UserResponseBody();
	}
}
