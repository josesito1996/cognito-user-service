package com.samy.service.cognitoapp.service;

import com.samy.service.cognitoapp.model.Usuario;
import com.samy.service.cognitoapp.model.request.UserRequestBody;
import com.samy.service.cognitoapp.model.response.UserResponseBody;

public interface UsuarioService extends ICrud<Usuario, String> {

    public Usuario registrarUsuario(UserRequestBody body);

    public Usuario buscarPorNombreUsuario(String userName);

    public Usuario buscarPorUserName(String userName);

    public Usuario buscarPorCorreo(String correo);

    public UserResponseBody getUsuarioByUserName(String userName);
}
