package com.samy.service.cognitoapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samy.service.cognitoapp.model.request.UserRequestBody;
import com.samy.service.cognitoapp.model.response.UserResponseBody;
import com.samy.service.cognitoapp.service.CognitoService;

@RestController
@RequestMapping("/api-usuario")
public class UsuarioController {

	@Autowired
	CognitoService cognitoService;

	@PostMapping("/createUser")
	public UserResponseBody crearUsuario(@Valid @RequestBody UserRequestBody request) {
		request.setNombreUsuario(request.getCorreo());
		return cognitoService.registrarUsuario(request);
	}
	
	@GetMapping("/deleteUser/{userName}")
	public UserResponseBody eliminarUsuario(@PathVariable String userName) {

		return cognitoService.eliminarUsuario(userName);
	}

}
