package com.samy.service.cognitoapp.service.impl;

import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.samy.service.cognitoapp.model.Seguimiento;
import com.samy.service.cognitoapp.repository.GenericRepo;
import com.samy.service.cognitoapp.repository.SeguimientoRepo;
import com.samy.service.cognitoapp.service.SeguimientoService;

@Service
public class SeguimientoServiceImpl extends CrudImpl<Seguimiento, String>
    implements SeguimientoService {

  @Autowired
  private SeguimientoRepo repo;

  @Override
  protected GenericRepo<Seguimiento, String> getRepo() {
    return repo;
  }

  @Override
  public Seguimiento listarPorEmpresaYUsuario(String empresa, String usuario) {
    return repo.findByEmpresaAndUsuario(empresa, usuario).stream()
        .sorted(Comparator.comparing(Seguimiento::getFechaIngreso).reversed())
        .findFirst().orElse(Seguimiento.builder().build());
  }


}
