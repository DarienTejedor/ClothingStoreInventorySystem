import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {UserResponse } from '../model/user.model';
import { TemporaryPasswordResponse } from '../model/temporary-password.model';
import { UserPageResponse } from '../model/user-page.model';
import { UserData } from '../model/user-request.model';
import { UserInfoRequest } from '../model/user-info-request.model';
import { UserPasswordRequest } from '../model/user-password-request.model';
import { UserPermissionsRequest } from '../model/user-permissions-request.model';
import { Store, StorePageResponse } from '../../stores/models/store.model';
import { UserRoleRequest } from '../model/user-role-request.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/users'; // Ajusta si usas environments

  constructor(private http: HttpClient) {}

  // Obtener usuarios paginados
  getUsers(searchTerm: string = ''): Observable<UserPageResponse> {
    let params = new HttpParams()
      if(searchTerm){
        params = params.set('search', searchTerm);
      }
    return this.http.get<UserPageResponse>(this.apiUrl, { params });
  }

  //Obtiene un usuario por ID
  getUserById(id: number): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.apiUrl}/${id}`);
  }

  // Obtener roles autorizados
  getAssignableRoles() {
    return this.http.get<any>('http://localhost:8080/roles/assignable');
  }

  // Crear nuevo usuario
  createUser(user: UserData): Observable<UserResponse> {
    return this.http.post<UserResponse>(this.apiUrl, user);
  }

  //Actualiza informacion (name y document)
  updateUserInfo(id: number, userData: UserInfoRequest): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${this.apiUrl}/${id}`, userData);
  }

  //Actualiza permisos (roleId y storeId)
  updateUserPermissions(id: number, permissionsData: UserPermissionsRequest): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${this.apiUrl}/${id}/permissions`, permissionsData);
  }

  updateRole(id: number, roleData: UserRoleRequest): Observable<UserResponse> {
    return this.http.put<UserResponse>(`${this.apiUrl}/${id}/role`, roleData);
  }

  //Actualiza password
  updateUserPassword(id: number, passwordData: UserPasswordRequest): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${id}/password`, passwordData);
  }

  resetPassword(id: number): Observable<TemporaryPasswordResponse> {
    return this.http.put<TemporaryPasswordResponse>(`${this.apiUrl}/${id}/reset-password`, {});
  }

  // Si tienes un endpoint para eliminar/desactivar por ID
  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getRoles(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/roles');
  }

  getStores(): Observable<StorePageResponse> {
    return this.http.get<StorePageResponse>('http://localhost:8080/stores');
  }
}