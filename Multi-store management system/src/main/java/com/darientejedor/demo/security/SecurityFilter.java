package com.darientejedor.demo.security;

import com.darientejedor.demo.domain.users.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null){
            var token = authHeader.replace("Bearer ","");
            var userName = tokenService.getSubject(token);
            if (userName != null){
                //Token validado
                var user = userRepository.findByLoginUser(userName);
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}




//Intercepta las peticiones y verifica el token
//
//Extrae el encabezado Authorization.
//
//Si el token existe y es válido:
//
//Extrae el usuario (subject) desde el token.
//
//Busca el usuario en la base de datos.
//
//Crea una instancia de UsernamePasswordAuthenticationToken y lo pone en el contexto de seguridad, indicando que el usuario está autenticado.
//
//Si no hay token o no es válido, la solicitud sigue sin autenticar.