package com.samy.service.cognitoapp.model.response;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
@ToString
public class UsuariosPorEmpresaResponse implements Serializable {

  private static final long serialVersionUID = -404172266560358993L;

  private String empresa;

  private String fechaCreacion;

  private List<String> usuarios;
}
