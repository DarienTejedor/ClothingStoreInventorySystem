export interface AuthRequest{
    loginUser: string;
    password: string;
}

export interface AuthResponse{
    token: string;
    name: string;
    role: string;
}