import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Store, StorePageResponse } from "../models/store.model";
import { StoreRequest } from "../models/store-request.model";

@Injectable({
    providedIn: 'root'
})
export class StoreService {
  private apiUrl = 'http://localhost:8080/stores'; // Ajusta a tu ruta real

  constructor(private http: HttpClient) { }

  // Obtener todas las tiendas
  getStores(searchTerm: string = ''): Observable<StorePageResponse> {
    let params = new HttpParams;

    if(searchTerm){
      params = params.set('search', searchTerm);
    }

    return this.http.get<StorePageResponse>(this.apiUrl, { params });
  }

  // Crear una tienda
  createStore(storeData: StoreRequest): Observable<Store> {
    return this.http.post<Store>(this.apiUrl, storeData);
  }

  updateStore(id: number, storeData: StoreRequest): Observable<Store> {
    return this.http.put<Store>(`${this.apiUrl}/${id}`, storeData);
  }

  // Eliminar una tienda
  deleteStore(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}