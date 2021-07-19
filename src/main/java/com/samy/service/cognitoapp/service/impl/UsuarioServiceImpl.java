package com.samy.service.cognitoapp.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.samy.service.cognitoapp.exception.NotFoundException;
import com.samy.service.cognitoapp.model.Usuario;
import com.samy.service.cognitoapp.model.request.UserRequestBody;
import com.samy.service.cognitoapp.model.response.UserResponseBody;
import com.samy.service.cognitoapp.repository.GenericRepo;
import com.samy.service.cognitoapp.repository.UsuarioRepo;
import com.samy.service.cognitoapp.service.UsuarioService;

@Service
public class UsuarioServiceImpl extends CrudImpl<Usuario, String> implements UsuarioService {

    @Autowired
    private UsuarioRepo repo;

    @Autowired
    private UserRequestBuilder builder;

    @Override
    protected GenericRepo<Usuario, String> getRepo() {
        return repo;
    }

    @Override
    public Usuario registrarUsuario(UserRequestBody body) {
        Usuario usuario = builder.transformFromUserRequestBody(body);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setEstado(true);
        return registrar(usuario);
    }

    @Override
    public Usuario buscarPorNombreUsuario(String userName) {
        Usuario usuario = repo.findByNombreUsuarioAndEstado(userName, true);
        if (usuario == null) {
            throw new NotFoundException(
                    "Usuario con el nombre " + userName + " no existe en la base de datos");
        }
        return usuario;
    }

    @Override
    public Usuario buscarPorUserName(String userName) {

        return repo.findByNombreUsuario(userName);
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        return repo.findByCorreo(correo);
    }

    @Override
    public UserResponseBody getUsuarioByUserName(String userName) {
        Usuario usuario = buscarPorNombreUsuario(userName);
        return UserResponseBody.builder()
                .id(usuario.getIdUsuario())
                .datosUsuario(usuario.getNombres().concat(" ").concat(usuario.getApellidos()).toUpperCase())
                .nombreUsuario(usuario.getNombreUsuario())
                .build();
    }

}
