package com.samy.service.cognitoapp.model.response;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class ColaboradorAdminReponse implements Serializable {

	private static final long serialVersionUID = -325020054383256577L;

	private String id;
	
	private String datos;
	
	private String userName;
	
	private String fechaRegistro;
	
	private String horaRegistro;
	
	private String rol;
	
	private boolean estado;
	
	private List<ModuloResponse> accesos;
	
}
