package com.samy.service.cognitoapp.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samy.service.cognitoapp.exception.BadRequestException;
import com.samy.service.cognitoapp.model.ColaboradorTable;
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

}
