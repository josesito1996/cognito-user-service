package com.samy.service.cognitoapp.model;

import java.io.Serializable;
import java.util.List;

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
public class Modulo implements Serializable{

	private static final long serialVersionUID = -7946781432415559399L;

	@DynamoDBAttribute
	private String name;

	@DynamoDBAttribute
	private String path;
	
	@DynamoDBAttribute
	private List<ElementosModulo> items;

}
