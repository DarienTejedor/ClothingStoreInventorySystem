import { HttpClient } from "@angular/common/http";
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
  getStores(): Observable<Store[]> {
    return this.http.get<Store[]>(this.apiUrl);
  }

  getStore(idStore: number): Observable<Store> {
    return this.http.get<Store>(`${this.apiUrl}/${idStore}`)
  }

  // Crear una tienda
  createStore(store: Store): Observable<Store> {
    return this.http.post<Store>(this.apiUrl, store);
  }

  // Eliminar una tienda
  deleteStore(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}