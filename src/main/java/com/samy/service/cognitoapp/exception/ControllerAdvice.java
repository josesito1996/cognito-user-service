package com.samy.service.cognitoapp.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse notFoundException(NotFoundException ex) {
		return new ErrorResponse(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), ex.getMessage(), new ArrayList<>());
	}
	
	@ExceptionHandler(FoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse foundException(FoundException ex) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), ex.getMessage(), new ArrayList<>());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		log.info("Dentro de la validacion : " + ex);
		List<ErrorDetail> details = new ArrayList<ErrorDetail>();
		ex.getBindingResult().getFieldErrors().forEach(item -> {
			details.add(new ErrorDetail(item.getField(), item.getDefaultMessage()));
		});
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(), "Validacion de campos", details);
	}

}
