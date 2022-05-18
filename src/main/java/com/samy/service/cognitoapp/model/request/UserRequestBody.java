package com.samy.service.cognitoapp.model.request;

import static com.samy.service.cognitoapp.utils.Constant.regexEmail;
import static com.samy.service.cognitoapp.utils.Constant.regexPassword;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class UserRequestBody {

	private String idUsuario;

	@NotNull
	@NotEmpty
	private String nombres;

	@NotNull
	@NotEmpty
	private String apellidos;

	@JsonIgnore
	private String nombreUsuario;

	@NotNull
	@NotEmpty
	@Pattern(regexp = regexEmail, message = "No es un correo valido")
	private String correo;

	@Size(min = 8, message = "Minimo 8 caracteres")
	@NotNull
	@NotEmpty
	@Pattern(regexp = regexPassword, message = "La contraseña debe tener caracteres especiales")
	// caracteres especiales")
	private String contraseña;

	@NotNull
	private Boolean terminos;

	@NotNull
	@NotEmpty
	private String empresa;
	
	private String tipo;

	@Valid
	private List<ColaboradoresBody> colaboradores;
}
