import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { AuthRequest, AuthResponse } from "../models/auth.model";

@Injectable({
    providedIn: 'root'
})
export class AuthService{

    private apiUrl = 'http://localhost:8080/login';

    constructor(private http: HttpClient) {}

    login(credentials: AuthRequest){
        return this.http.post<AuthResponse>(`${this.apiUrl}`, credentials)
    }

    refreshToken(refreshToken: string){
        return this.http.post<AuthResponse>(`${this.apiUrl}/refresh`, { refreshToken })
    }

    getRole(): string | null {
        return sessionStorage.getItem('role');
    }

    getName(): string | null {
        return sessionStorage.getItem('name');
    }

    isGeneralAdmin(): boolean {
        return this.getRole() === 'ROLE_GENERAL_ADMIN';
    }

    isStoreAdmin(): boolean {
    return this.getRole() === 'ROLE_STORE_ADMIN';
    }

    logout(): void {
        sessionStorage.clear();
    }

}