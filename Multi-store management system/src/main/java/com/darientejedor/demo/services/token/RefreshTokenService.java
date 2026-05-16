package com.darientejedor.demo.services.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService implements  ITokenService{
    @Value("${jwt.refreshExpirationMs}")
}
