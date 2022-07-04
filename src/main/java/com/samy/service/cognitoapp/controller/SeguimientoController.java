package com.samy.service.cognitoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.samy.service.cognitoapp.model.Seguimiento;
import com.samy.service.cognitoapp.service.SeguimientoService;

@RestController
@RequestMapping("/api-seguimiento")
public class SeguimientoController {

  @Autowired
  private SeguimientoService service;

  @GetMapping("/listByEmpresa/{empresa}/{usuario}")
  public Seguimiento listarPorEmpresaYUsuario(@PathVariable String empresa,
      @PathVariable String usuario) {
    return service.listarPorEmpresaYUsuario(empresa, usuario);
  }

}
