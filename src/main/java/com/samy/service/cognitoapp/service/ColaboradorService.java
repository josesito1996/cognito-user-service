package com.samy.service.cognitoapp.service;

import java.util.List;

import com.samy.service.cognitoapp.model.ColaboradorTable;

public interface ColaboradorService extends ICrud<ColaboradorTable, String> {

	List<ColaboradorTable> buscarPorIdUsuario(String idUsuario);
	
	ColaboradorTable buscarPorId(String idColaborador);
	
	boolean colaboradorExiste(String correo);
	
}
