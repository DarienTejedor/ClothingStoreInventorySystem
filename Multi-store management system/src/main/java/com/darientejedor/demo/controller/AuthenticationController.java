package com.darientejedor.demo.controller;


import com.darientejedor.demo.domain.users.User;
import com.darientejedor.demo.domain.dtos.UserAuthenticationData;
import com.darientejedor.demo.domain.dtos.JWTokenData;
import com.darientejedor.demo.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity userAuth(@RequestBody @Valid UserAuthenticationData userAuthData){
        Authentication tokenAuth = new UsernamePasswordAuthenticationToken(userAuthData.loginUser(), userAuthData.password());

        var AuthenthicatedUser = authenticationManager.authenticate(tokenAuth);
        var JWToken = tokenService.generateToken((User)AuthenthicatedUser.getPrincipal());
        return ResponseEntity.ok(new JWTokenData(JWToken));
    }
}
