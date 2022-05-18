package com.samy.service.cognitoapp.utils;

import static com.samy.service.cognitoapp.utils.Utils.buildDateExpiration;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtil {

    private static Map<String, Object> buildHeaders() {
        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put("Authentication", "JwtToken");
        headers.put("header1", "( ͡° ͜ʖ ͡°)");
        headers.put("header2", "Si llegas hasta aqui ლ(´ڡ`ლ)");
        headers.put("header3", "Wiiiiiiiiiiiiii");
        return headers;
    }

    public static String getJwtFromObjectAuthentication(Map<String, Object> mapObject) {
        return Jwts.builder().setHeader(buildHeaders()).setIssuer("SAMY_APPLICATION")
                .setSubject("Samy System authorization token JWT").addClaims(mapObject)
                .setExpiration(buildDateExpiration(LocalDateTime.now())).compact();
    }

    public static Map<String, Object> decodedJwt(String token) {
        Map<String, Object> newMap;
        try {
            newMap = new HashMap<String, Object>();
            DecodedJWT decoded = JWT.decode(token);
            Map<String, Claim> payLoad = decoded.getClaims();
            payLoad.forEach((key, value) -> {
                newMap.put(key, value);
            });
            return newMap;
        } catch (JWTDecodeException e) {
            log.error("Error al decodificar Token : " + e.getMessage());
            return new HashMap<String, Object>();
        }
    }
}
