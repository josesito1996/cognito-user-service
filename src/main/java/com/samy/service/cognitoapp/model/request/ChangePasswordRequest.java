package com.samy.service.cognitoapp.model.request;

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
public class ChangePasswordRequest {

	@NotNull
	@NotEmpty
	private String token;
	
	@NotNull
	@NotEmpty
	private String userName;
	
	@NotNull
	@NotEmpty
	private String oldPassword;
	
	@NotNull
	@NotEmpty
	private String newPassword;
	
}
