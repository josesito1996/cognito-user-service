package com.samy.service.cognitoapp.model;

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
public class UserSignInRequest {

	private String userName;
	
	private String email;
	
	private String password;
	
}
