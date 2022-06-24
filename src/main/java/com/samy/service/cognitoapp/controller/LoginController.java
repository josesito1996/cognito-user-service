package com.samy.service.cognitoapp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samy.service.cognitoapp.model.request.LoginRequest;
import com.samy.service.cognitoapp.model.response.TokenAuthResponse;
import com.samy.service.cognitoapp.service.CognitoService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api-login")
@Slf4j
public class LoginController {

	@Autowired
	private CognitoService service;
	
	@PostMapping("/auth")
	public TokenAuthResponse login(@Valid @RequestBody LoginRequest request) {
		log.info("LoginRequest {}", request);
		return service.login(request);
	}
	
}
