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
@NoArgsConstructor
@Setter
@ToString
public class LoginRequest implements Serializable {

	private static final long serialVersionUID = 537814233718996779L;

	@NotNull
	@NotEmpty
	private String userName;
	
	@NotNull
	@NotEmpty
	private String password;
	
}
