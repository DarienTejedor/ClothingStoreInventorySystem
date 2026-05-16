package com.darientejedor.demo.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.darientejedor.demo.domain.users.repository.UserRepository;
import com.darientejedor.demo.exceptions.UnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/login/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null){
            var token = authHeader.replace("Bearer ","");
            try {
                var userName = tokenService.getSubject(token);
                if (userName != null){
                    //Token validado
                    var userOptional = userRepository.findByLoginUser(userName);
                    if (userOptional.isPresent()) {
                        var user = userOptional.get();
                        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (TokenExpiredException e){
                resolver.resolveException(request, response, null, new UnauthorizedException("Token Expirado") {}
                );
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}

