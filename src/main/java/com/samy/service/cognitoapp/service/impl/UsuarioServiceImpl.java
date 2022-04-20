package com.samy.service.cognitoapp.service.impl;

import static com.samy.service.cognitoapp.utils.JwtUtil.decodedJwt;
import static com.samy.service.cognitoapp.utils.JwtUtil.getJwtFromObjectAuthentication;
import static com.samy.service.cognitoapp.utils.Utils.buildBodyForToken;
import static com.samy.service.cognitoapp.utils.Utils.buildBodyColaboratorForToken;
import static com.samy.service.cognitoapp.utils.Utils.cleanId;
import static com.samy.service.cognitoapp.utils.Utils.messageWelcomeHtmlBuilder;
import static com.samy.service.cognitoapp.utils.Utils.messageWelcomeColaboratorHtmlBuilder;
import static com.samy.service.cognitoapp.utils.Utils.numberToLocalDateTime;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samy.service.cognitoapp.exception.BadRequestException;
import com.samy.service.cognitoapp.exception.NotFoundException;
import com.samy.service.cognitoapp.model.ColaboradorTable;
import com.samy.service.cognitoapp.model.Usuario;
import com.samy.service.cognitoapp.model.request.ColaboratorRequest;
import com.samy.service.cognitoapp.model.request.UserRequestBody;
import com.samy.service.cognitoapp.model.response.UserResponseBody;
import com.samy.service.cognitoapp.repository.GenericRepo;
import com.samy.service.cognitoapp.repository.UsuarioRepo;
import com.samy.service.cognitoapp.service.CognitoService;
import com.samy.service.cognitoapp.service.ColaboradorService;
import com.samy.service.cognitoapp.service.LambdaService;
import com.samy.service.cognitoapp.service.UsuarioService;
import com.samy.service.cognitoapp.utils.Password;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsuarioServiceImpl extends CrudImpl<Usuario, String> implements UsuarioService {

	@Autowired
	private UsuarioRepo repo;

	@Autowired
	private LambdaService lambdaService;

	@Autowired
	private UserRequestBuilder builder;

	@Autowired
	private CognitoService cognitoService;

	@Autowired
	private ColaboradorService colaboradorService;

	@Override
	protected GenericRepo<Usuario, String> getRepo() {
		return repo;
	}

	@Override
	public Usuario buscarPorId(String id) {
		Optional<Usuario> usuarioOptional = verPorId(cleanId(id));
		if (!usuarioOptional.isPresent()) {
			throw new NotFoundException("Usuario con el ID " + id + " no existe en la BD");
		}
		return usuarioOptional.get();
	}

	@Override
	public void registrarColaboratorDesdeSami(ColaboratorRequest request) {
		log.info("registrarColaboratorDesdeSami.request {}",request);
		Usuario usuario = buscarPorNombreUsuario(request.getUserName());
		List<ColaboradorTable> colaboradores = colaboradorService.buscarPorIdUsuario(usuario.getIdUsuario());
		Long countColaboradores = colaboradores.stream().filter(item -> item.getCorreo().equals(request.getMail()))
				.count();
		log.info("registrarColaboratorDesdeSami.colaboradores {}", colaboradores);
		boolean existeColaborador = colaboradorService.colaboradorExiste(request.getMail());
		log.info("registrarColaboratorDesdeSami.existeColaborador {}", existeColaborador);
		Usuario usuarioEncontrado = buscarPorUserName(request.getMail());
		log.info("registrarColaboratorDesdeSami.UsuarioEncontrado {}", usuarioEncontrado);
		if (usuarioEncontrado != null) {
			throw new BadRequestException(
					"Ya existe un Usuario con el mismo mail del colaborador : " + request.getUserName());
		}
		if (countColaboradores != 0 && existeColaborador) {
			throw new BadRequestException("Ya existe un colaborador con el mismo UserName : " + request.getUserName());
		}
		Password password = new Password();
		password.setLongitud(10);
		password.setContrasena(request.getMail());
		ColaboradorTable colaborador = colaboradorService
				.registrar(ColaboradorTable.builder().nombres(request.getNames()).apellidos(request.getLastName())
						.correo(request.getMail()).password(password.generarPassword()).empresa(usuario.getEmpresa())
						.idUsuario(usuario.getIdUsuario())
						.fechaRegistro(LocalDateTime.now())
						.passwordChanged(false)
						.estado(true).validado(false).eliminado(false).build());

		JsonObject obj = new JsonObject();
		obj.addProperty("emailFrom", "notificacion.sami@sidetechsolutions.com");
		obj.addProperty("subject", "Correo de bienvenida");
		obj.addProperty("emailTo", colaborador.getCorreo());
		obj.addProperty("content", messageWelcomeColaboratorHtmlBuilder(colaborador,
				getJwtFromObjectAuthentication(buildBodyColaboratorForToken(colaborador))));
		JsonElement resultMainSend = lambdaService.mailSendWithLambda(obj.toString()).get("code");
		String code = resultMainSend.getAsString();
		log.info("mailSendWithLambda : " + code);
		if (!code.equals("202")) {
			throw new BadRequestException("El correo no se envió");
		}

	}

	@Override
	public Usuario registrar(Usuario body) {
		Usuario usuario = repo.findByNombreUsuario(body.getNombreUsuario());
		if (usuario != null) {
			throw new BadRequestException("El usuario " + usuario.getNombreUsuario() + " ya existe en la BD");
		}
		return repo.save(body);
	}

	@Override
	public Usuario registrarUsuario(UserRequestBody body) {
		Usuario usuario = builder.transformFromUserRequestBody(body);
		usuario.setFechaCreacion(LocalDateTime.now());
		usuario.setEstado(true);
		usuario.setEliminado(false);
		usuario.setValidado(true);
		return registrar(usuario);
	}

	@Override
	public Usuario buscarPorNombreUsuario(String userName) {
		Usuario usuario = buscarPorUserName(userName);
		if (usuario == null) {
			throw new NotFoundException("Usuario con el nombre " + userName + " no existe en la base de datos");
		}
		return usuario;
	}

	@Override
	public Usuario buscarPorUserNameAndEstado(String userName, boolean estado) {
		return repo.findByNombreUsuarioAndEstado(userName, true);
	}

	@Override
	public Usuario buscarPorCorreo(String correo) {
		return repo.findByCorreo(correo);
	}

	@Override
	public UserResponseBody getUsuarioByUserName(String userName) {
		Usuario usuario = buscarPorUserName(userName);
		if (usuario == null) {
			return new UserResponseBody();
		}
		String nombres = usuario.getNombres();
		String apellidos = usuario.getApellidos();
		String nuevoNombre = nombres;
		String nuevoApellido = apellidos;
		if (nombres.contains(" ")) {
			nuevoNombre = nombres.substring(0, nombres.indexOf(" "));
		}
		if (apellidos.contains(" ")) {
			nuevoApellido = apellidos.substring(0, apellidos.indexOf(" "));
		}
		return UserResponseBody.builder().id(usuario.getIdUsuario())
				.datosUsuario(nuevoNombre.concat(" ").concat(nuevoApellido)).nombreUsuario(usuario.getNombreUsuario())
				.claveCambiada(true)
				.tipo("USUARIO").build();
	}

	@Override
	public Usuario registrarUsuarioV2(UserRequestBody requestBody) {
		log.info("UsuarioServiceImpl.registrarUsuarioV2");
		/*
		 * JsonObject obj = new JsonObject(); JsonObject objMailBody = new JsonObject();
		 * objMailBody.addProperty("nombreUsuario",
		 * extraerNombreUsuario(requestBody.getNombreUsuario()));
		 * objMailBody.addProperty("nombres", requestBody.getNombres());
		 * objMailBody.addProperty("apellidos", requestBody.getApellidos());
		 * objMailBody.addProperty("password", requestBody.getContraseña());
		 * obj.addProperty("httpStatus", "VALID_EMAIL"); obj.addProperty("emailToFind",
		 * requestBody.getNombreUsuario()); obj.add("mailBody", objMailBody);
		 * 
		 * JsonObject lambdaResponse =
		 * lambdaService.validEmailAndRegisterWithLambda(obj.toString()); boolean estado
		 * = lambdaResponse.get("estado").getAsBoolean();
		 */
		Usuario usuario = builder.transformFromUserRequestBody(requestBody);
		usuario.setEliminado(false);
		usuario.setEstado(false);
		usuario.setValidado(false);
		usuario.setFechaCreacion(LocalDateTime.now());
		Usuario newUsuario = registrar(usuario);
		JsonObject obj = new JsonObject();
		obj.addProperty("emailFrom", "notificacion.sami@sidetechsolutions.com");
		obj.addProperty("subject", "Correo de bienvenida");
		obj.addProperty("emailTo", requestBody.getNombreUsuario());
		obj.addProperty("content",
				messageWelcomeHtmlBuilder(newUsuario, getJwtFromObjectAuthentication(buildBodyForToken(usuario))));
		JsonElement resultMainSend = lambdaService.mailSendWithLambda(obj.toString()).get("code");
		String code = resultMainSend.getAsString();
		log.info("mailSendWithLambda : " + code);
		if (code.equals("202")) {
			return newUsuario;
		}
		throw new BadRequestException("Error al enviar el correo de autenticacion !!!!");
	}

	@Transactional
	@Override
	public boolean activarUsuario(String tokenKey) {
		log.info("UsuarioServiceImpl.activarUsuario");
		log.info("TokenKey : " + tokenKey);
		boolean resultado = false;
		Map<String, Object> decodedToken = decodedJwt(tokenKey);
		if (decodedToken.isEmpty()) {
			throw new BadRequestException("No se puede autenticar el usuario !!!");
		}
		String idUsuario = decodedToken.get("id").toString();
		log.info("idUsuario : " + idUsuario);
		long exp = Long.parseLong(decodedToken.get("exp").toString());
		LocalDateTime fechaExp = numberToLocalDateTime(exp);
		if (fechaExp.isAfter(LocalDateTime.now())) {
			log.info("fechaExp : " + fechaExp);
			Usuario usuario = buscarPorId(idUsuario);
			log.info("Usuario encontrado : " + usuario.toString());
			log.info("Usuario validado : " + usuario.isValidado());
			if (usuario.isValidado()) {
				throw new BadRequestException("El usuario se encuentra activo !!!");
			} else {
				usuario.setEstado(true);
				usuario.setEliminado(false);
				usuario.setValidado(true);
				modificar(usuario);
				UserResponseBody response = cognitoService
						.registrarUsuarioV2(UserRequestBody.builder().nombres(usuario.getNombres())
								.apellidos(usuario.getApellidos()).nombreUsuario(usuario.getNombreUsuario())
								.correo(usuario.getCorreo()).contraseña(usuario.getContrasena()).terminos(true)
								.tipo("USUARIO").empresa(usuario.getEmpresa()).colaboradores(null).build());
				resultado = response.getId() != null;
			}
		} else {
			eliminarUsuario(idUsuario);
			throw new BadRequestException("El token ha expirado por favor registrate nuevamente");
		}
		return resultado;
	}

	@Override
	public Usuario eliminarUsuario(String id) {
		Usuario usuario = buscarPorId(id);
		usuario.setEstado(false);
		usuario.setValidado(false);
		usuario.setEliminado(true);
		return modificar(usuario);
	}

	@Override
	public Usuario buscarPorNombreUsuarioPorCognito(String userName) {
		log.info("UsuarioServiceImpl.buscarPorNombreUsuarioPorCognito");
		log.info("UserName : " + userName);
		return buscarPorNombreUsuario(userName);
	}

	@Override
	public boolean activarColaborador(String tokenKey) {
		log.info("UsuarioServiceImpl.activarColaborador");
		log.info("TokenKey : " + tokenKey);
		boolean resultado = false;
		Map<String, Object> decodedToken = decodedJwt(tokenKey);
		if (decodedToken.isEmpty()) {
			throw new BadRequestException("No se puede autenticar el usuario !!!");
		}
		String idColaborador = decodedToken.get("id").toString();
		log.info("idColaborador : " + idColaborador);
		long exp = Long.parseLong(decodedToken.get("exp").toString());
		LocalDateTime fechaExp = numberToLocalDateTime(exp);
		if (fechaExp.isAfter(LocalDateTime.now())) {
			log.info("fechaExp : " + fechaExp);
			ColaboradorTable colaboradorTable = colaboradorService.buscarPorId(idColaborador.replace("\"", ""));
			log.info("Usuario encontrado : " + colaboradorTable.toString());
			log.info("Usuario validado : " + colaboradorTable.isValidado());
			if (colaboradorTable.isValidado()) {
				throw new BadRequestException("El usuario se encuentra activo !!!");
			} else {
				colaboradorTable.setEstado(true);
				colaboradorTable.setEliminado(false);
				colaboradorTable.setValidado(true);
				ColaboradorTable colaboradorModified = colaboradorService.modificar(colaboradorTable);
				UserResponseBody response = cognitoService.registrarUsuarioV2(UserRequestBody.builder()
						.nombres(colaboradorModified.getNombres()).apellidos(colaboradorModified.getApellidos())
						.nombreUsuario(colaboradorModified.getCorreo()).correo(colaboradorModified.getCorreo())
						.contraseña(colaboradorModified.getPassword()).terminos(true).tipo("COLABORADOR")
						.empresa(colaboradorModified.getEmpresa()).colaboradores(null).build());
				resultado = response.getId() != null;
			}
		} else {
			eliminarUsuario(idColaborador);
			throw new BadRequestException("El token ha expirado por favor registrate nuevamente");
		}
		return resultado;
	}

	@Override
	public Usuario buscarPorUserName(String userName) {
		return repo.findByNombreUsuario(userName);
	}
}
