package com.darientejedor.demo.services.token;

import com.darientejedor.demo.domain.token.RefreshToken;

public interface ITokenService {
    RefreshToken createRefreshToken(Long id);

    boolean isTokenExpired(RefreshToken token);

}
