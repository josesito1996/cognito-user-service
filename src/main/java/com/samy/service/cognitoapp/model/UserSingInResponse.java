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
public class UserSingInResponse {
	private String accessToken;
	private String idToken;
	private String refreshToken;
	private String tokenType;
	private Integer expiresIn;
}
