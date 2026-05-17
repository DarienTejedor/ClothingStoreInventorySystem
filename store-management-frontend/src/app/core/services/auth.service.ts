import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { AuthRequest, AuthResponse } from "../models/auth.model";

@Injectable({
    providedIn: 'root'
})
export class AuthService{

    private apiUrl = 'http://localhost:8080/';

    constructor(private http: HttpClient) {}

    login(credentials: AuthRequest){
        return this.http.post<AuthResponse>(`${this.apiUrl}login`, credentials)
    }

    refreshToken(refreshToken: string){
        return this.http.post<AuthResponse>(`${this.apiUrl}/refresh`, { refreshToken })
    }

}