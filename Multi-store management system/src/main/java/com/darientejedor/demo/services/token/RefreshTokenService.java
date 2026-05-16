package com.darientejedor.demo.services.token;

import com.darientejedor.demo.domain.refreshToken.RefreshToken;
import com.darientejedor.demo.domain.refreshToken.dto.RefreshTokenResponse;
import com.darientejedor.demo.domain.refreshToken.repository.RefreshTokenRepository;
import com.darientejedor.demo.domain.users.User;
import com.darientejedor.demo.domain.users.repository.UserRepository;
import com.darientejedor.demo.exceptions.UnauthorizedException;
import com.darientejedor.demo.exceptions.ValidationException;
import com.darientejedor.demo.security.TokenService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService implements IRefreshTokenService {
    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public RefreshTokenService(RefreshTokenRepository tokeRepo,
                               UserRepository userRepo, TokenService tokenService) {
        this.refreshTokenRepository = tokeRepo;
        this.userRepository = userRepo;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional
    public RefreshToken createRefreshToken (Long userId) {
        refreshTokenRepository.deleteByUserId(userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Usuario no encontrado"));

        var token = new RefreshToken ();

        token.setUser(user);
        token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        token.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(token);
    }

    @Override
    public boolean isTokenExpired (RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    @Override
    public RefreshTokenResponse refreshToken(String requestToken) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestToken)
                .orElseThrow(() ->
                        new UnauthorizedException("Token de actualización inválido"));

        if (isTokenExpired(refreshToken)) {
            refreshTokenRepository.delete(refreshToken);
            throw new UnauthorizedException("Token de actualización expirado");
        }
        String newAccessToken =
                tokenService.generateToken(refreshToken.getUser());
        return new RefreshTokenResponse(newAccessToken);
    }
}
