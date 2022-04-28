package com.samy.service.cognitoapp.model.request;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AccesoRequest implements Serializable {
	
	private static final long serialVersionUID = -3684495549169287741L;

	@NotNull
	@NotEmpty
	private String name;

	@NotNull
	@NotEmpty
	private String path;
}
