package com.darientejedor.demo.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.darientejedor.demo.domain.users.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    /// Trae la contraseña desde las propiedades o variables de entorno

    @Value("${api.security.secret}")
    private String apiPassword;

    /// Genera el Token

    public String generateToken(User user){
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiPassword);
            return JWT.create()
                    .withIssuer("InvManagementSystem")
                    .withSubject(user.getLoginUser())
                    .withClaim("id", user.getId())
                    .withExpiresAt(tokenExpiration())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException();
        }

    }

    /// Verifica el token

    public String getSubject(String token){
        if (token == null) throw new RuntimeException();
        Algorithm algorithm = Algorithm.HMAC256(apiPassword);
        DecodedJWT verifer = JWT.require(algorithm)
                .withIssuer("InvManagementSystem")
                .build()
                .verify(token);
        if (verifer.getSubject() == null) {
            throw new RuntimeException("Invalid verification");
        }
        return verifer.getSubject();
    }

    ///  define el tiempo de expirado del token
    /// se usa intant ya que permite medir el tiempo en nanosegundos y es inmutable

    private Instant tokenExpiration(){
        return LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("-05:00"));
    }

}
