import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TemporaryPasswordResponse, UserData, UserPageResponse, UserResponse } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/users'; // Ajusta si usas environments

  constructor(private http: HttpClient) {}

  // Obtener usuarios paginados
  getUsers(searchTerm: string = ''): Observable<any> {
    let params = new HttpParams()
      if(searchTerm){
        params = params.set('search', searchTerm);
      }
    return this.http.get<any>(this.apiUrl, { params });
  }

  // Obtener roles autorizados
  getAssignableRoles() {
    return this.http.get<any>('http://localhost:8080/roles/assignable');
  }

  // Crear nuevo usuario
  createUser(user: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, user);
  }

  //Actualiza informacion (name y document)
  updateUserInfo(id: number, userData: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, userData);
  }

  //Actualiza permisos (roleId y storeId)
  updateUserPermissions(id: number, permissionsData: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}/permissions`, permissionsData);
  }

  //Actualiza password
  updateUserPassword(id: number, passwordData: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}/password`, passwordData);
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

  getStores(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/stores');
  }
}