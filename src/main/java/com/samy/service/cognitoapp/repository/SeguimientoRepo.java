package com.samy.service.cognitoapp.repository;

import java.util.List;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import com.samy.service.cognitoapp.model.Seguimiento;

@EnableScan
public interface SeguimientoRepo extends GenericRepo<Seguimiento, String> {
    
  List<Seguimiento> findByEmpresa(String empresa);
  
}
