package com.samy.service.cognitoapp.repository;

import java.util.List;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;

import com.samy.service.cognitoapp.model.ColaboradorTable;

@EnableScan
public interface ColaboradorRepo extends GenericRepo<ColaboradorTable,String> {
    
	List<ColaboradorTable> findByIdUsuario(String idUsuario);
	
	ColaboradorTable findOneByCorreo(String correo);
	
}
