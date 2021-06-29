package com.samy.service.cognitoapp.model.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@ToString
public class UserRequestBody {

	@NotNull
	@NotEmpty
	private String nombres;

	@NotNull
	@NotEmpty
	private String apellidos;

	@NotNull
	@NotEmpty
	private String nombreUsuario;
	
	@NotNull
	@NotEmpty
	private String correo;

	@NotNull
	@NotEmpty
	private String contraseña;

	@NotNull
	private Boolean terminos;

	@NotNull
	@NotEmpty
	private String empresa;

	@Valid
	private List<ColaboradoresBody> colaboradores;
}
