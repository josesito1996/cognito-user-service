package com.samy.service.cognitoapp.model.response;

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
@NoArgsConstructor
@Setter
@ToString
public class TokenAuthResponse implements Serializable {

	private static final long serialVersionUID = 5395266296867343818L;
	
	private String token;
	
	private UserResponseBody data;
	
}
