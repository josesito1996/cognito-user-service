package com.samy.service.cognitoapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.samy.service.cognitoapp.exception.BadRequestException;
import com.samy.service.cognitoapp.service.UsuarioService;

@Controller
@RequestMapping("/token-auth")
public class TokenController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping(path = "/authenticate")
    public RedirectView redirectToPage(@RequestParam String tokenKey) {
        if (usuarioService.activarUsuario(tokenKey)) {
            return new RedirectView("https://develop.d1m4mh8zc59yft.amplifyapp.com/");
        } else {
            throw new BadRequestException("No de puedo validar el usuario");
        }
    }
}
