package com.darientejedor.demo.services.token;

import com.darientejedor.demo.domain.refreshToken.RefreshToken;
import com.darientejedor.demo.domain.refreshToken.dto.RefreshTokenResponse;
import com.darientejedor.demo.security.dtos.JWTokenResponse;

public interface IRefreshTokenService {
    RefreshToken createRefreshToken(Long id);

    boolean isTokenExpired(RefreshToken token);

    RefreshTokenResponse refreshToken(String requestToken);

}
