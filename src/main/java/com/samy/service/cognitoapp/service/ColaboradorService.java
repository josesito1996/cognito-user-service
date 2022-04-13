package com.samy.service.cognitoapp.service;

import java.util.List;

import com.samy.service.cognitoapp.model.ColaboradorTable;
import com.samy.service.cognitoapp.model.response.UserResponseBody;

public interface ColaboradorService extends ICrud<ColaboradorTable, String> {

	List<ColaboradorTable> buscarPorIdUsuario(String idUsuario);
	
	ColaboradorTable buscarPorCorreo(String correo);
	
	ColaboradorTable buscarPorId(String idColaborador);
	
	boolean colaboradorExiste(String correo);
	UserResponseBody getUsuarioByUserName(String userName);
	
}
