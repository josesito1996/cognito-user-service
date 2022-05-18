package com.samy.service.cognitoapp.model.request;

import static com.samy.service.cognitoapp.utils.Constant.regexEmail;
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
public class ColaboradoresBody {

	@NotNull
	@NotEmpty
	private String nombre;

	@NotNull
	@NotEmpty
	@Pattern(regexp = regexEmail, message = "No es un correo valido")
	private String correo;
}
