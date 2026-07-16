package com.darientejedor.demo.services.token;

import com.darientejedor.demo.domain.refreshToken.RefreshToken;
import com.darientejedor.demo.domain.refreshToken.dto.RefreshTokenResponse;

public interface IRefreshTokenService {
    RefreshToken createRefreshToken(Long id);

    boolean isTokenExpired(RefreshToken token);

    RefreshTokenResponse refreshToken(String requestToken);

}
