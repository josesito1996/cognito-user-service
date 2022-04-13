package com.samy.service.cognitoapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.samy.service.cognitoapp.service.UsuarioService;

@SpringBootApplication
public class CognitoAwsServiceApplication implements CommandLineRunner {

	@Autowired
	private UsuarioService usuarioService;

	public static void main(String[] args) {
		SpringApplication.run(CognitoAwsServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// usuarioUpdate();
	}

	public void usuarioUpdate() {
		usuarioService.listar().forEach(item -> {
			item.setTipo("USUARIO");
			usuarioService.modificar(item);
		});
	}
}
