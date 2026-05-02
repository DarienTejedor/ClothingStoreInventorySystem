import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { Store } from '../../../../core/models/store.model';
import { StoreService } from '../../../../core/services/store.service';
import { ReactiveFormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { StoreFormComponent } from '../../components/store-form/store-form.component';

@Component({
  selector: 'app-store-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, StoreFormComponent],
  templateUrl: './store-list.component.html',
  styleUrl: './store-list.component.css',
})
export class StoreListComponent implements OnInit{
  @ViewChild(StoreFormComponent) storeFormChild!: StoreFormComponent;
  isModalOpen = false;
  isEditing = false;
  isLoading: boolean = true;
  searchTerm: string = '';
  selectedStoreId: number | null = null;
  stores: Store[] = [];

  
  
  onSearch(event: any): void {
      const value = event.target.value;
      this.searchTerm = value;
    
      this.loadStores(this.searchTerm);
    }
    
  constructor (
    private storeService: StoreService,
    private cdr: ChangeDetectorRef
  ){}
    
  offLoading(){
    this.cdr.detectChanges();
    return this.isLoading = false;
  }

  ngOnInit(): void {
    this.loadStores();
  }
  
  //MODAL ---------------------------------------------------------

openModal() {
    this.isEditing = false;
    this.isModalOpen = true;
    this.storeFormChild.resetForm(); // Le decimos al hijo que limpie
  }

  closeModal(){
    this.isModalOpen = false;
  }
  
  //EDITAR ---------------------------------------------------------
  
  openedEditModal(store: Store) {
    this.isEditing = true;
    this.selectedStoreId = store.id;
    this.isModalOpen = true;
    // Le pasamos los datos al hijo
    setTimeout(() => this.storeFormChild.setData(store), 0); 
  }

  handleSave(storeData: any) {
    if (this.isEditing && this.selectedStoreId) {
      this.storeService.updateStore(this.selectedStoreId, storeData).subscribe({
        next: () => {
          alert('Tienda actualizada');
          this.finishProcess();
        }
      });
    } else {
      this.storeService.createStore(storeData).subscribe({
        next: () => {
          alert('Tienda creada');
          this.finishProcess();
        }
      });
    }
  }
  
  private finishProcess() {
      this.isModalOpen = false;
      this.isEditing = false;
      this.selectedStoreId = null;
      this.loadStores();
    }


  //LISTAR ---------------------------------------------------------
  
  loadStores(term: string = ''): void {
    this.isLoading = true;
    
    this.storeService.getStores(term)
    .pipe(
      finalize(() => this.offLoading())
    )
    .subscribe({
      next: (response: any) => {
        this.stores = response.content;
        this.offLoading();
      },
      error: (e) => {
        console.error('Error al cargar tiendas', e);
        this.offLoading();
      }
    })
  }
  
  //DELETE ---------------------------------------------------------

  deleteStore(id: number | undefined): void {
    if (!id) return;
    
    if (confirm('¿Estás seguro de eliminar esta tienda?')) {
      this.storeService.deleteStore(id).subscribe(() => {
        this.stores = this.stores.filter(s => s.id !== id);
      });
    }
  }

}
