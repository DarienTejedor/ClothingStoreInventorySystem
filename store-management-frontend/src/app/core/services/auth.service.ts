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

    //Datos del usuario logueado

    getUserId(): number{
        return Number(sessionStorage.getItem('id'));
    }

    getRole(): string{
        return sessionStorage.getItem('role') ?? '';
    }

    getName(): string {
        return sessionStorage.getItem('name') ?? '';
    }

    getToken(): string {
        return sessionStorage.getItem('token') ?? '';
    }

    setToken(token: string): void {
        sessionStorage.setItem('token', token);
    }
    getRefreshToken(): string {
        return sessionStorage.getItem('refreshToken') ?? '';
    }

    setRefreshToken(refreshToken: string): void {
        sessionStorage.setItem('refreshToken', refreshToken);
    }

    // Guardado de sesión
    saveSession(response: AuthResponse): void {
        sessionStorage.setItem('token', response.token);
        sessionStorage.setItem('refreshToken', response.refreshToken);
        sessionStorage.setItem('name', response.name);
        sessionStorage.setItem('role', response.role);
        sessionStorage.setItem('id', response.id.toString());
        sessionStorage.setItem('storeId', response.storeId?.toString() ?? '');
        sessionStorage.setItem('storeName', response.storeName ?? '');
    }

    //Datos de los Roles

    isGeneralAdmin(): boolean {
        return this.getRole() === 'ROLE_GENERAL_ADMIN';
    }

    isStoreAdmin(): boolean {
    return this.getRole() === 'ROLE_STORE_ADMIN';
    }

    isCashier(): boolean {
        return this.getRole() === 'ROLE_CASHIER';
    }

    // Datos del store

    getStoreId(): number | null {
        const value = sessionStorage.getItem('storeId');
        return value ? Number(value) : null;
    }

    getStoreName(): string {
        return sessionStorage.getItem('storeName') ?? '';
    }

    //Sesion

    logout(): void {
        sessionStorage.clear();
    }

}