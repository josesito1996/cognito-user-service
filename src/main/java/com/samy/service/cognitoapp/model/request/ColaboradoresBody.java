package com.samy.service.cognitoapp.model.request;

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
public class ColaboradoresBody {

	@NotNull
	@NotEmpty
	private String nombre;

	@NotNull
	@NotEmpty
	private String correo;
}
