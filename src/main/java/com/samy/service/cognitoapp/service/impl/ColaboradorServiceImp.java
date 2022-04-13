package com.samy.service.cognitoapp.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samy.service.cognitoapp.exception.BadRequestException;
import com.samy.service.cognitoapp.model.ColaboradorTable;
import com.samy.service.cognitoapp.model.response.UserResponseBody;
import com.samy.service.cognitoapp.repository.ColaboradorRepo;
import com.samy.service.cognitoapp.repository.GenericRepo;
import com.samy.service.cognitoapp.service.ColaboradorService;

@Service
public class ColaboradorServiceImp extends CrudImpl<ColaboradorTable, String> implements ColaboradorService {

	@Autowired
	private ColaboradorRepo repo;

	@Override
	protected GenericRepo<ColaboradorTable, String> getRepo() {
		return repo;
	}

	@Override
	public List<ColaboradorTable> buscarPorIdUsuario(String idUsuario) {

		return repo.findByIdUsuario(idUsuario);
	}

	@Override
	public boolean colaboradorExiste(String correo) {
		ColaboradorTable colaboradorTable = repo.findOneByCorreo(correo);
		return colaboradorTable != null;
	}

	@Override
	public ColaboradorTable buscarPorId(String idColaborador) {
		Optional<ColaboradorTable> colaboradorOptional = verPorId(idColaborador);
		if (colaboradorOptional.isEmpty()) {
			throw new BadRequestException("Colaborador con id "+ idColaborador + " no existe");
		}
		return colaboradorOptional.get();
	}

	@Override
	public ColaboradorTable buscarPorCorreo(String correo) {
		
		return repo.findOneByCorreo(correo);
	}

	@Override
	public UserResponseBody getUsuarioByUserName(String userName) {
		ColaboradorTable colaborador = buscarPorCorreo(userName);
		if (colaborador==null) {
			return new UserResponseBody();
		}
		String nombres = colaborador.getNombres();
		String apellidos = colaborador.getApellidos();
		String nuevoNombre = nombres;
		String nuevoApellido = apellidos;
		if (nombres.contains(" ")) {
			nuevoNombre = nombres.substring(0, nombres.indexOf(" "));
		}
		if (apellidos.contains(" ")) {
			nuevoApellido = apellidos.substring(0, apellidos.indexOf(" "));
		}
		return UserResponseBody.builder().id(colaborador.getIdColaborador())
				.datosUsuario(nuevoNombre.concat(" ").concat(nuevoApellido)).nombreUsuario(colaborador.getCorreo())
				.tipo("COLABORADOR")
				.build();
	}

}
