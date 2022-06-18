package com.samy.service.cognitoapp.utils;

public class Constant {

	public static final String REGEX_UUID = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$";
	
	public static final String regexEmail = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)"
			+ "+[a-zA-Z]{2,6}$";

	public static final String lambdaMailSenderNombre = "lambda-mailSender-service-template";
	
	public static final String regexPassword = "^(?=.*\\d)(?=.*[\\u0021-\\u002b\\u003c-\\u0040])(?=.*[A-Z])(?=.*[a-z])\\S{8,16}$";
	
	public static final String TOKEN_PATH_USER = "token-auth/authenticate?tokenKey=";
	
	public static final String TOKEN_PATH_COLABORATOR = "token-auth/authenticateColaborator?tokenKey=";
	
}
