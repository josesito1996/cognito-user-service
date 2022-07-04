package com.samy.service.cognitoapp.service;

import com.samy.service.cognitoapp.model.Seguimiento;

public interface SeguimientoService extends ICrud<Seguimiento, String> {

  Seguimiento listarPorEmpresaYUsuario(String empresa, String usuario);
  
}
