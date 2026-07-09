import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserData, UserPageResponse, UserResponse } from '../model/user.model';

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

  // Crear nuevo usuario
  createUser(user: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, user);
  }

  updateUser(id: number, user: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, user);
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