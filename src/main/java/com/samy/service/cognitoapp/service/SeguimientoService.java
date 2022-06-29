package com.samy.service.cognitoapp.service;

import java.util.List;
import com.samy.service.cognitoapp.model.Seguimiento;

public interface SeguimientoService extends ICrud<Seguimiento, String> {

  List<Seguimiento> listarPorEmpresa(String empresa);
  
}
