package com.samy.service.cognitoapp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@DynamoDBTable(tableName = "colaborador")
@Getter
@NoArgsConstructor
@Setter
@ToString
public class ColaboradorTable implements Serializable {

	private static final long serialVersionUID = 2803839972544976507L;
	
	@DynamoDBHashKey
	@DynamoDBAutoGeneratedKey
	@DynamoDBAttribute(attributeName = "id_colaborador")
	private String idColaborador;
	
	@DynamoDBAttribute
	private String nombres;

	@DynamoDBAttribute
	private String apellidos;
	
	@DynamoDBAttribute
	private String correo;
	
	@DynamoDBAttribute
	private String password;

	@DynamoDBAttribute
	private String empresa;
	
	@DynamoDBAttribute
	private String idUsuario;
	
	@DynamoDBAttribute
	private boolean estado;
	
	@DynamoDBAttribute
	private boolean eliminado;
	
	@DynamoDBAttribute
    private boolean validado;
	
	@DynamoDBTypeConverted( converter = LocalDateTimeConverter.class )
	@DynamoDBAttribute
	private LocalDateTime fechaRegistro;
	
	@DynamoDBAttribute
	private String rol;
	
	@DynamoDBAttribute
	private boolean passwordChanged;
}
