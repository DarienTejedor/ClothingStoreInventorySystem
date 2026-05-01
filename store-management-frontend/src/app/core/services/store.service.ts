import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Store } from "../models/store.model";

@Injectable({
    providedIn: 'root'
})
export class StoreService {
    private apiUrl = 'http://localhost:8080/stores'; // Ajusta a tu ruta real

  constructor(private http: HttpClient) { }

  // Obtener todas las tiendas
  getStores(searchTerm: string = ''): Observable<any> {
    let params = new HttpParams;

    if(searchTerm){
      params = params.set('search', searchTerm);
    }

    return this.http.get<any>(this.apiUrl, { params });
  }

  // Crear una tienda
  createStore(storeData: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, storeData);
  }

  updateStore(id: number, storeData: any): Observable<any>{
    return this.http.put<any>(`${this.apiUrl}/${id}`, storeData)
  }

  // Eliminar una tienda
  deleteStore(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}