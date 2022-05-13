package com.samy.service.cognitoapp.model.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserResponseBody {

	private String id;
	
	private String datosUsuario;
	
	private String nombreUsuario;
	
	private String tipo;
	
	private String rol;
	
	private String empresa;
	
	private boolean claveCambiada;
	
	List<ModuloResponse> accesos;

}
