import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { Store, StorePageResponse } from '../../models/store.model';
import { StoreService } from '../../services/store.service';
import { ReactiveFormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { StoreFormComponent } from '../../components/store-form/store-form.component';
import { AlertService } from '../../../../shared/services/alert.service';
import { StoreRequest } from '../../models/store-request.model';

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

  
  constructor (
    private storeService: StoreService,
    private alertService: AlertService,
    private cdr: ChangeDetectorRef
  ){}
  
  onSearch(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.searchTerm = value;

    this.loadStores(this.searchTerm);
  }
    
    
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

  handleSave(storeData: StoreRequest): void {
    if (this.isEditing && this.selectedStoreId) {
      this.storeService.updateStore(this.selectedStoreId, storeData).subscribe({
        next: () => {
          this.alertService.success('Tienda actualizada');
          this.finishProcess();
        }
      });
    } else {
      this.storeService.createStore(storeData).subscribe({
        next: () => {
          this.alertService.success('Tienda creada');
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
      next: (response: StorePageResponse) => {
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

  async deleteStore(id: number | undefined){
    if (!id) return;
    
    const result = await this.alertService.confirmDelete('¿Deseas eliminar esta tienda?');

    if(result.isConfirmed) {
      this.storeService.deleteStore(id).subscribe({
        next: () => {
          this.alertService.success('Tienda eliminada');
          this.loadStores(this.searchTerm);
        },
        error: (e) => {
          console.error('Error al eliminar tienda', e);
          this.alertService.error('Error al eliminar tienda');
        }
      });
    }
  }
}
