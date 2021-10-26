package com.samy.service.cognitoapp.service.impl;

import static com.samy.service.cognitoapp.utils.JwtUtil.decodedJwt;
import static com.samy.service.cognitoapp.utils.JwtUtil.getJwtFromObjectAuthentication;
import static com.samy.service.cognitoapp.utils.Utils.buildBodyForToken;
import static com.samy.service.cognitoapp.utils.Utils.cleanId;
import static com.samy.service.cognitoapp.utils.Utils.messagewelcomeHtmlBuilder;
import static com.samy.service.cognitoapp.utils.Utils.numberToLocalDateTime;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samy.service.cognitoapp.exception.BadRequestException;
import com.samy.service.cognitoapp.exception.NotFoundException;
import com.samy.service.cognitoapp.model.Usuario;
import com.samy.service.cognitoapp.model.request.UserRequestBody;
import com.samy.service.cognitoapp.model.response.UserResponseBody;
import com.samy.service.cognitoapp.repository.GenericRepo;
import com.samy.service.cognitoapp.repository.UsuarioRepo;
import com.samy.service.cognitoapp.service.CognitoService;
import com.samy.service.cognitoapp.service.LambdaService;
import com.samy.service.cognitoapp.service.UsuarioService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsuarioServiceImpl extends CrudImpl<Usuario, String> implements UsuarioService {

    @Autowired
    private UsuarioRepo repo;

    @Autowired
    private LambdaService lambdaService;

    @Autowired
    private UserRequestBuilder builder;

    @Autowired
    private CognitoService cognitoService;

    @Override
    protected GenericRepo<Usuario, String> getRepo() {
        return repo;
    }

    @Override
    public Usuario buscarPorId(String id) {
        Optional<Usuario> usuarioOptional = verPorId(cleanId(id));
        if (!usuarioOptional.isPresent()) {
            throw new NotFoundException("Usuario con el ID " + id + " no existe en la BD");
        }
        return usuarioOptional.get();
    }

    @Override
    public Usuario registrar(Usuario body) {
        Usuario usuario = repo.findByNombreUsuario(body.getNombreUsuario());
        if (usuario != null) {
            throw new BadRequestException(
                    "El usuario " + usuario.getNombreUsuario() + " ya existe en la BD");
        }
        return repo.save(body);
    }

    @Override
    public Usuario registrarUsuario(UserRequestBody body) {
        Usuario usuario = builder.transformFromUserRequestBody(body);
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setEstado(true);
        usuario.setEliminado(false);
        usuario.setValidado(true);
        return registrar(usuario);
    }

    @Override
    public Usuario buscarPorNombreUsuario(String userName) {
        Usuario usuario = repo.findByNombreUsuario(userName);
        if (usuario == null) {
            throw new NotFoundException(
                    "Usuario con el nombre " + userName + " no existe en la base de datos");
        }
        return usuario;
    }

    @Override
    public Usuario buscarPorUserNameAndEstado(String userName, boolean estado) {
        return repo.findByNombreUsuarioAndEstado(userName, true);
    }

    @Override
    public Usuario buscarPorCorreo(String correo) {
        return repo.findByCorreo(correo);
    }

    @Override
    public UserResponseBody getUsuarioByUserName(String userName) {
        Usuario usuario = buscarPorNombreUsuario(userName);
        return UserResponseBody
                .builder().id(usuario.getIdUsuario()).datosUsuario(usuario.getNombres().concat(" ")
                        .concat(usuario.getApellidos()).toUpperCase())
                .nombreUsuario(usuario.getNombreUsuario()).build();
    }

    @Override
    public Usuario registrarUsuarioV2(UserRequestBody requestBody) {
        log.info("UsuarioServiceImpl.registrarUsuarioV2");
        /*
         * JsonObject obj = new JsonObject(); JsonObject objMailBody = new JsonObject();
         * objMailBody.addProperty("nombreUsuario",
         * extraerNombreUsuario(requestBody.getNombreUsuario()));
         * objMailBody.addProperty("nombres", requestBody.getNombres());
         * objMailBody.addProperty("apellidos", requestBody.getApellidos());
         * objMailBody.addProperty("password", requestBody.getContraseña());
         * obj.addProperty("httpStatus", "VALID_EMAIL"); obj.addProperty("emailToFind",
         * requestBody.getNombreUsuario()); obj.add("mailBody", objMailBody);
         * 
         * JsonObject lambdaResponse =
         * lambdaService.validEmailAndRegisterWithLambda(obj.toString()); boolean estado
         * = lambdaResponse.get("estado").getAsBoolean();
         */
        Usuario usuario = builder.transformFromUserRequestBody(requestBody);
        usuario.setEliminado(false);
        usuario.setEstado(false);
        usuario.setValidado(false);
        usuario.setFechaCreacion(LocalDateTime.now());
        Usuario newUsuario = registrar(usuario);
        JsonObject obj = new JsonObject();
        obj.addProperty("emailFrom", "jbedoyafox@sidetechsolutions.com");
        obj.addProperty("subject", "Correo de bienvenida");
        obj.addProperty("emailTo", requestBody.getNombreUsuario());
        obj.addProperty("content", messagewelcomeHtmlBuilder(newUsuario,
                getJwtFromObjectAuthentication(buildBodyForToken(usuario))));
        JsonElement resultMainSend = lambdaService.mailSendWithLambda(obj.toString()).get("code");
        String code = resultMainSend.getAsString();
        log.info("mailSendWithLambda : " + code);
        if (code.equals("202")) {
            return newUsuario;
        }
        throw new BadRequestException("Error al enviar el correo de autenticacion !!!!");
    }

    @Transactional
    @Override
    public boolean activarUsuario(String tokenKey) {
        log.info("UsuarioServiceImpl.activarUsuario");
        log.info("TokenKey : " + tokenKey);
        boolean resultado = false;
        Map<String, Object> decodedToken = decodedJwt(tokenKey);
        if (decodedToken.isEmpty()) {
            throw new BadRequestException("No se puede autenticar el usuario !!!");
        }
        String idUsuario = decodedToken.get("id").toString();
        log.info("idUsuario : " + idUsuario);
        long exp = Long.parseLong(decodedToken.get("exp").toString());
        LocalDateTime fechaExp = numberToLocalDateTime(exp);
        if (fechaExp.isAfter(LocalDateTime.now())) {
            log.info("fechaExp : " + fechaExp);
            Usuario usuario = buscarPorId(idUsuario);
            log.info("Usuario encontrado : " + usuario.toString());
            log.info("Usuario validado : " + usuario.getValidado());
            if (usuario.getValidado()) {
                throw new BadRequestException("El usuario se encuentra activo !!!");
            } else {
                usuario.setEstado(true);
                usuario.setEliminado(false);
                usuario.setValidado(true);
                modificar(usuario);
                UserResponseBody response = cognitoService.registrarUsuarioV2(UserRequestBody
                        .builder().nombres(usuario.getNombres()).apellidos(usuario.getApellidos())
                        .nombreUsuario(usuario.getNombreUsuario()).correo(usuario.getCorreo())
                        .contraseña(usuario.getContrasena()).terminos(true)
                        .empresa(usuario.getEmpresa()).colaboradores(null).build());
                resultado = response.getId() != null;
            }
        } else {
            eliminarUsuario(idUsuario);
            throw new BadRequestException("El token ha expirado por favor registrate nuevamente");
        }
        return resultado;
    }

    @Override
    public Usuario eliminarUsuario(String id) {
        Usuario usuario = buscarPorId(id);
        usuario.setEstado(false);
        usuario.setValidado(false);
        usuario.setEliminado(true);
        return modificar(usuario);
    }

    @Override
    public Usuario buscarPorNombreUsuarioPorCognito(String userName) {
        log.info("UsuarioServiceImpl.buscarPorNombreUsuarioPorCognito");
        log.info("UserName : " + userName);
        return buscarPorNombreUsuario(userName);
    }
}
