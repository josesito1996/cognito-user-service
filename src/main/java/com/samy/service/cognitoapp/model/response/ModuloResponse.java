package com.samy.service.cognitoapp.model.response;

import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class ModuloResponse implements Serializable{

	private static final long serialVersionUID = -3959603794683150343L;
	
	private String name;
	
	private String path;
	
	private List<ElementosModuloResponse> items;
	
}
