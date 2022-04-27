package com.samy.service.cognitoapp.service;


import com.samy.service.cognitoapp.model.Usuario;
import com.samy.service.cognitoapp.model.request.ColaboratorRequest;
import com.samy.service.cognitoapp.model.request.UserRequestBody;
import com.samy.service.cognitoapp.model.response.UserResponseBody;

public interface UsuarioService extends ICrud<Usuario, String> {

    public Usuario buscarPorId(String id);
    
    public Usuario eliminarUsuario(String id);
    
    public Usuario registrarUsuario(UserRequestBody body);

    public Usuario buscarPorNombreUsuario(String userName);
    
    public Usuario buscarPorUserName(String userName);
    
    public Usuario buscarPorUserNameAndEstado(String userName, boolean estado);
    
    public Usuario buscarPorNombreUsuarioPorCognito(String userName);
    

    public Usuario buscarPorCorreo(String correo);

    public UserResponseBody getUsuarioByUserName(String userName);
    
    ///Para la version 2
    
    public Usuario registrarUsuarioV2(UserRequestBody requestBody);
    
    public void registrarColaboratorDesdeSami(ColaboratorRequest request);
    
    public boolean activarUsuario(String tokenKey);
    
    public boolean activarColaborador(String tokenKey);
}
