package com.samy.service.cognitoapp.model;

import java.io.Serializable;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@DynamoDBDocument
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Colaborador implements Serializable {

	private static final long serialVersionUID = -7766392681747261246L;

	@DynamoDBAttribute
	private String nombre;

	@DynamoDBAttribute
	private String correo;
}
