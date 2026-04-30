import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Store } from '../../../../core/models/store.model';
import { StoreService } from '../../../../core/services/store.service';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-store-list',
  imports: [CommonModule],
  templateUrl: './store-list.component.html',
  styleUrl: './store-list.component.css',
})
export class StoreListComponent implements OnInit{
  stores: Store[] = [];
  store: Store | null = null;
  isLoading: boolean = true;
  

  constructor (
    private storeService: StoreService,
    private cdr: ChangeDetectorRef
  ){}

  ngOnInit(): void {
    this.loadStores();
  }

  loadStores(): void {
    this.isLoading = true;
    this.storeService.getStores()
    .pipe(
      finalize(() => this.offLoading())
    )
    .subscribe({
      next: (response: any) => {
        console.log('Tiendas cargadas: ', response);
        this.stores = response.content;
        this.offLoading();
      },
      error: (e) => {
        console.error('Error al cargar tiendas', e);
        this.offLoading();
      }
    })
  }

loadStore(idStore: number | undefined): void {
  if (!idStore) return;

  this.storeService.getStore(idStore)
    .pipe(
      finalize(() => this.offLoading())
    )
    .subscribe({
      next: (data) => {
        this.store = data;
        this.cdr.detectChanges();
      },
      error: (e) => {
        console.error('Error al traer la tienda', e);
      }
    });
}

  offLoading(){
    this.cdr.detectChanges();
    return this.isLoading = false;
  }

  deleteStore(id: number | undefined): void {
    if (!id) return;
    
    if (confirm('¿Estás seguro de eliminar esta tienda?')) {
      this.storeService.deleteStore(id).subscribe(() => {
        // Filtramos la lista local para no tener que recargar todo
        this.stores = this.stores.filter(s => s.id !== id);
      });
    }
  }

  

}
