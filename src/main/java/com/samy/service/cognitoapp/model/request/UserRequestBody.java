package com.samy.service.cognitoapp.model.request;

//import static com.samy.service.cognitoapp.utils.Constant.regexPassword;
import static com.samy.service.cognitoapp.utils.Constant.regexEmail;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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

	private String idUsuario;

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
	@Pattern(regexp = regexEmail, message = "No es un correo valido")
	private String correo;

	@NotNull
	@NotEmpty
	// @Pattern(regexp = regexPassword, message = "La contraseña debe tener
	// caracteres especiales")
	private String contraseña;

	@NotNull
	private Boolean terminos;

	@NotNull
	@NotEmpty
	private String empresa;

	@Valid
	private List<ColaboradoresBody> colaboradores;
}
