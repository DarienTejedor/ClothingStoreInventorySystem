export interface AuthRequest{
    loginUser: string;
    password: string;
}

export interface AuthResponse{
    token: string;
    refreshToken: string;
    name: string;
    role: string;
}

export interface RefreshTokenRequest{
    refreshToken: string;
}

export interface RefreshTokenResponse{
    token: string;
}