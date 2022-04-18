package com.samy.service.cognitoapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samy.service.cognitoapp.model.Usuario;
import com.samy.service.cognitoapp.model.request.ChangePasswordRequest;
import com.samy.service.cognitoapp.model.request.ColaboratorRequest;
import com.samy.service.cognitoapp.model.request.UserRequestBody;
import com.samy.service.cognitoapp.model.response.UserResponseBody;
import com.samy.service.cognitoapp.service.CognitoService;
import com.samy.service.cognitoapp.service.ColaboradorService;
import com.samy.service.cognitoapp.service.UsuarioService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api-usuario")
@Slf4j
public class UsuarioController {

	@Autowired
	CognitoService cognitoService;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	ColaboradorService colaboradorService;

	@PostMapping("/createUser")
	public UserResponseBody crearUsuario(@Valid @RequestBody UserRequestBody request) {
		request.setNombreUsuario(request.getCorreo());
		return cognitoService.registrarUsuario(request);
	}

	@PostMapping("/createUserv2")
	public Usuario createUserV2(@Valid @RequestBody UserRequestBody request) {
		request.setNombreUsuario(request.getCorreo());
		return usuarioService.registrarUsuarioV2(request);
	}
	
	@PutMapping("/changePassword")
	public void updateUser(@Valid @RequestBody ChangePasswordRequest request) {
		cognitoService.cambiarPasswordUsuario(request);
	}

	@PostMapping("/addColaborator")
	public void createColaborator(@Valid @RequestBody ColaboratorRequest request) {
		usuarioService.registrarColaboratorDesdeSami(request);
	}

	@GetMapping("/getUser/{userName}")
	public UserResponseBody verUsuarioPorUserName(@PathVariable String userName) {
		log.info("GetUser {}", userName);
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

	@GetMapping("/deleteUser/{userName}")
	public UserResponseBody eliminarUsuario(@PathVariable String userName) {

		return cognitoService.eliminarUsuario(userName);
	}

}
