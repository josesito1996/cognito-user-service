package com.samy.service.cognitoapp.model;

import java.time.LocalDateTime;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.samy.service.cognitoapp.config.LocalDateTimeConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@DynamoDBTable(tableName = "usuarios")
@Getter
@NoArgsConstructor
@Setter
@ToString
public class Usuario {

	@DynamoDBHashKey
	@DynamoDBAutoGeneratedKey
	@DynamoDBAttribute(attributeName = "id_usuario")
	private String idUsuario;
	
	@DynamoDBAttribute
	private String nombres;

	@DynamoDBAttribute
	private String apellidos;

	@DynamoDBAttribute
	private String nombreUsuario;
	
	@DynamoDBAttribute
	private String correo;

	@DynamoDBAttribute
	private String contrasena;

	@DynamoDBAttribute
	private Boolean terminos;

	@DynamoDBAttribute
	private String empresa;

	@DynamoDBAttribute
	private List<Colaborador> colaboradores;
	
	@DynamoDBTypeConverted( converter = LocalDateTimeConverter.class )
	@DynamoDBAttribute
	private LocalDateTime fechaCreacion;
	
	@DynamoDBAttribute
	private Boolean estado;
	
	@DynamoDBAttribute
	private Boolean eliminado;
	
	@DynamoDBAttribute
    private boolean validado;
	
	@DynamoDBAttribute
	private String rol;
	
	/**
	 * Tipo: 
	 * usuario -> usuario normal
	 * colaborador -> colaborador
	 * 
	 */
	@DynamoDBAttribute
	private String tipo;
}
