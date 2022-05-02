package com.samy.service.cognitoapp.model.response;

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
public class ElementosModuloResponse implements Serializable {

	private static final long serialVersionUID = -344321423223865941L;

	@NotNull
	@NotEmpty
	private String key;
	
	@NotNull
	@NotEmpty
	private String item;
	
	private boolean estado;
}
