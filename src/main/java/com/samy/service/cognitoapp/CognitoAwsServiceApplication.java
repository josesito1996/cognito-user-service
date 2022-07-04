package com.samy.service.cognitoapp;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.JsonObject;
import com.samy.service.cognitoapp.service.ColaboradorService;
import com.samy.service.cognitoapp.service.UsuarioService;

@SpringBootApplication
public class CognitoAwsServiceApplication implements CommandLineRunner {

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ColaboradorService colaboradorService;

	public static void main(String[] args) {
		SpringApplication.run(CognitoAwsServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//usuarioUpdate();
		//colaboradorUpdate();
		testJson();
	}

	public void testJson() {
		JsonObject obj = new JsonObject();
		JsonObject objMain = new JsonObject();
		objMain.addProperty("detail", "Jola");
		obj.addProperty("id", 1);
		obj.addProperty("name", "Josesito!!!");
		obj.add("detalle", objMain);
		System.out.println(obj);
	}
	
	public void usuarioUpdate() {
		usuarioService.listar().forEach(item -> {
			//item.setRol("ADMIN");
			item.setAccesos(new ArrayList<>());
			item.getAccesos().forEach(acceso -> {
				acceso.setItems(new ArrayList<>());
			});
			usuarioService.modificar(item);
		});
	}
	public void colaboradorUpdate() {
		colaboradorService.listar().forEach(item -> {
			//item.setRol("COLABORADOR");
			item.setAccesos(new ArrayList<>());
			item.getAccesos().forEach(acceso -> {
				acceso.setItems(new ArrayList<>());
			});
			colaboradorService.modificar(item);
		});
	}
	
	public void testCompletable() {
		
	}
}
