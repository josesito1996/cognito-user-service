package com.samy.service.cognitoapp.model.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.samy.service.cognitoapp.utils.Constant.REGEX_UUID;

import java.io.Serializable;

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
@NoArgsConstructor
@ToString
public class ColaboradorDisableEnablaRequest implements Serializable {

	private static final long serialVersionUID = 5052528795751293770L;

	@NotNull
	@NotEmpty
	@Pattern(regexp = REGEX_UUID)
	private String id;

	private boolean estado;

}
