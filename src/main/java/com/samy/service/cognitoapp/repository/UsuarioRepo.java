package com.samy.service.cognitoapp.repository;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;

import com.samy.service.cognitoapp.model.Usuario;

@EnableScan
public interface UsuarioRepo extends GenericRepo<Usuario, String>{
	
	Usuario findByNombreUsuario(String userName);
	
	Usuario findByCorreo(String correo);
	
	Usuario findByNombreUsuarioAndEstado(String userName, Boolean estado);
}
