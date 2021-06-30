package com.samy.service.cognitoapp.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3548390125863434956L;

	public NotFoundException(String message) {
		super(message);
	}
}
