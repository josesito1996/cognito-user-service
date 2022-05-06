package com.samy.service.cognitoapp.service.impl;

import static com.samy.service.cognitoapp.utils.Utils.formatoFecha;
import static com.samy.service.cognitoapp.utils.Utils.formatoHora;
import static com.samy.service.cognitoapp.utils.Utils.transformToModulo;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.AdminDisableUserResult;
import com.amazonaws.services.cognitoidp.model.AdminEnableUserResult;
import com.samy.service.cognitoapp.exception.BadRequestException;
import com.samy.service.cognitoapp.model.ColaboradorTable;
import com.samy.service.cognitoapp.model.request.ColaboradorAccesoRequest;
import com.samy.service.cognitoapp.model.request.ColaboradorDisableEnablaRequest;
import com.samy.service.cognitoapp.model.response.ColaboradorAdminReponse;
import com.samy.service.cognitoapp.model.response.ColaboradorResponse;
import com.samy.service.cognitoapp.model.response.UserResponseBody;
import com.samy.service.cognitoapp.repository.ColaboradorRepo;
import com.samy.service.cognitoapp.repository.GenericRepo;
import com.samy.service.cognitoapp.service.ColaboradorService;
import com.samy.service.cognitoapp.utils.Utils;

@Service
public class ColaboradorServiceImp extends CrudImpl<ColaboradorTable, String> implements ColaboradorService {

	@Autowired
	private ColaboradorRepo repo;

	@Autowired
	private CognitoRequestBuilder builder;

	@Autowired
	private AWSCognitoIdentityProvider cognitoClient;

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
			throw new BadRequestException("Colaborador con id " + idColaborador + " no existe");
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
		if (colaborador == null) {
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
				.accesos(colaborador.getAccesos().stream().map(item -> Utils.transformToModulo(item))
						.collect(Collectors.toList()))
				.tipo("COLABORADOR").claveCambiada(colaborador.isPasswordChanged()).build();
	}

	@Override
	public ColaboradorResponse buscarPorUserName(String userName) {
		ColaboradorTable colaborador = buscarPorCorreo(userName);
		return ColaboradorResponse.builder().idColaborador(colaborador.getIdColaborador())
				.nombres(colaborador.getNombres()).apellidos(colaborador.getApellidos()).correo(colaborador.getCorreo())
				.password(colaborador.getPassword()).empresa(colaborador.getEmpresa())
				.idUsuario(colaborador.getIdUsuario()).estado(colaborador.isEstado())
				.eliminado(colaborador.isEliminado()).validado(colaborador.isValidado())
				.fechaRegistro(colaborador.getFechaRegistro()).passwordChanged(colaborador.isPasswordChanged()).build();
	}

	@Override
	public List<String> colaboradoresPorUsuario(String idUsuario) {
		List<ColaboradorTable> colaboradores = repo.findByIdUsuario(idUsuario);
		return colaboradores.stream().map(ColaboradorTable::getCorreo).collect(Collectors.toList());
	}

	@Override
	public List<ColaboradorAdminReponse> panelColaboradoresPorUsuario(String idUsuario) {

		List<ColaboradorTable> colaboradores = repo.findByIdUsuario(idUsuario);
		return colaboradores.stream()
				.sorted(Comparator.comparing(ColaboradorTable::isEstado).reversed())
				.map(colaborador -> transform(colaborador)).collect(Collectors.toList());
	}

	@Override
	public ColaboradorAdminReponse agregarAccesos(ColaboradorAccesoRequest request) {
		ColaboradorTable colaborador = buscarPorId(request.getId());
		colaborador.setAccesos(
				request.getAccesos().stream().map(item -> transformToModulo(item)).collect(Collectors.toList()));
		return transform(modificar(colaborador));
	}

	private ColaboradorAdminReponse transform(ColaboradorTable colaborador) {
		return ColaboradorAdminReponse.builder().id(colaborador.getIdColaborador())
				.datos(colaborador.getNombres().concat(" ").concat(colaborador.getApellidos()))
				.userName(colaborador.getCorreo()).fechaRegistro(formatoFecha(colaborador.getFechaRegistro()))
				.horaRegistro(formatoHora(colaborador.getFechaRegistro())).rol(colaborador.getRol())
				.estado(colaborador.isEstado()).accesos(colaborador.getAccesos().stream()
						.map(item -> Utils.transformToModulo(item)).collect(Collectors.toList()))
				.build();
	}

	@Override
	public ColaboradorAdminReponse cambiarEstado(ColaboradorDisableEnablaRequest request) {
		ColaboradorTable colaborador = buscarPorId(request.getId());
		colaborador.setEstado(request.isEstado());
		if (request.isEstado()) {
			//Habilitar
			AdminEnableUserResult result = builder.enableUser(colaborador.getCorreo(), cognitoClient);
			if (result.getSdkResponseMetadata() == null) {
				throw new BadRequestException("Error al deshabilitar el usuario " + colaborador.getCorreo());
			}
		} else {
			//Desabilitar
			AdminDisableUserResult result = builder.disableUser(colaborador.getCorreo(), cognitoClient);
			if (result.getSdkResponseMetadata() == null) {
				throw new BadRequestException("Error al deshabilitar el usuario " + colaborador.getCorreo());
			}
		}
		return transform(modificar(colaborador));
	}

}
