export interface AuthRequest{
    loginUser: string;
    password: string;
}

export interface AuthResponse{
    token: string;
    refreshToken: string;
    id: number;
    name: string;
    role: string;
    storeId: number | null;
    storeName: string | null;
}

export interface RefreshTokenRequest{
    refreshToken: string;
}

export interface RefreshTokenResponse{
    token: string;
}