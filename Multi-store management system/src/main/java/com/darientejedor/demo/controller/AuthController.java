package com.darientejedor.demo.controller;


import com.darientejedor.demo.domain.refreshToken.dto.RefreshTokenRequest;
import com.darientejedor.demo.domain.refreshToken.dto.RefreshTokenResponse;
import com.darientejedor.demo.domain.users.User;
import com.darientejedor.demo.domain.users.dto.UserAuthenticationData;
import com.darientejedor.demo.security.TokenService;
import com.darientejedor.demo.security.dtos.JWTokenResponse;
import com.darientejedor.demo.services.token.IRefreshTokenService;
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

import java.util.Map;

@RestController
@RequestMapping("login")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IRefreshTokenService refreshTokenService;

    @PostMapping
    public ResponseEntity<JWTokenResponse> userAuth(@RequestBody @Valid UserAuthenticationData userAuthData){
        Authentication tokenAuth = new UsernamePasswordAuthenticationToken(userAuthData.loginUser(), userAuthData.password());

        var authenthicatedUser = authenticationManager.authenticate(tokenAuth);

        User user = (User) authenthicatedUser.getPrincipal();
        var JWToken = tokenService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        String role = user.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok(new JWTokenResponse(JWToken, refreshToken.getToken(), user.getName(), role));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(refreshTokenService.refreshToken(request.refreshToken()));
    }
}
