import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { AuthRequest, AuthResponse } from "../models/auth.model";

@Injectable({
    providedIn: 'root'
})
export class AuthService{

    constructor(private http: HttpClient) {}

    private apiUrl = 'http://localhost:8080/login';
    
    login(credentials: AuthRequest){
        return this.http.post<AuthResponse>(this.apiUrl, credentials)
    }

}