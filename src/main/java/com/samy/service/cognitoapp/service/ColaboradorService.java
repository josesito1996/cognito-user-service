package com.samy.service.cognitoapp.service;

import java.util.List;

import com.samy.service.cognitoapp.model.ColaboradorTable;
import com.samy.service.cognitoapp.model.response.ColaboradorAdminReponse;
import com.samy.service.cognitoapp.model.response.ColaboradorResponse;
import com.samy.service.cognitoapp.model.response.UserResponseBody;

public interface ColaboradorService extends ICrud<ColaboradorTable, String> {

	List<ColaboradorTable> buscarPorIdUsuario(String idUsuario);

	ColaboradorTable buscarPorCorreo(String correo);

	ColaboradorResponse buscarPorUserName(String userName);

	ColaboradorTable buscarPorId(String idColaborador);

	boolean colaboradorExiste(String correo);

	UserResponseBody getUsuarioByUserName(String userName);

	List<String> colaboradoresPorUsuario(String idUsuario);

	/**
	 * Para ver loscolaboradores del usuario
	 */
	public List<ColaboradorAdminReponse> panelColaboradoresPorUsuario(String idUsuario);

}
