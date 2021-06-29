package com.samy.service.cognitoapp.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.samy.service.cognitoapp.model.Colaborador;
import com.samy.service.cognitoapp.model.Usuario;
import com.samy.service.cognitoapp.model.request.ColaboradoresBody;
import com.samy.service.cognitoapp.model.request.UserRequestBody;

@Component
public class UserRequestBuilder {

	public Usuario transformFromUserRequestBody(UserRequestBody requestBody) {
		return Usuario.builder()
				.idUsuario(requestBody.getIdUsuario())
				.nombres(requestBody.getNombres())
				.apellidos(requestBody.getApellidos())
				.nombreUsuario(requestBody.getNombreUsuario())
				.correo(requestBody.getCorreo())
				.contraseña(requestBody.getContraseña())
				.terminos(requestBody.getTerminos())
				.empresa(requestBody.getEmpresa())
				.colaboradores(transformToColaboradorBody(requestBody.getColaboradores()))
				.build();
	}
	
	private List<Colaborador> transformToColaboradorBody (List<ColaboradoresBody> colaboradoresBodies){
		return colaboradoresBodies.stream().map(this::transformToColaborador).collect(Collectors.toList());
	}
	
	private Colaborador transformToColaborador(ColaboradoresBody colaboradoresBody) {
		return Colaborador.builder()
				.correo(colaboradoresBody.getCorreo())
				.nombre(colaboradoresBody.getNombre())
				.build();
	}
}
