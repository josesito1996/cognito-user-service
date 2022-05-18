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
@Getter
@Setter
@NoArgsConstructor
@DynamoDBDocument
@ToString
public class ElementosModulo implements Serializable {

	private static final long serialVersionUID = 4508346316359231760L;

	@DynamoDBAttribute
	private String key;

	@DynamoDBAttribute
	private String item;
	
	@DynamoDBAttribute
	private boolean estado;
}
