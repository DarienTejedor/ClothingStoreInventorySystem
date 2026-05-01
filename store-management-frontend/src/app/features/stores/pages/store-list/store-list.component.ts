import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Store } from '../../../../core/models/store.model';
import { StoreService } from '../../../../core/services/store.service';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-store-list',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './store-list.component.html',
  styleUrl: './store-list.component.css',
})
export class StoreListComponent implements OnInit{
  isModalOpen = false;
  isEditing = false;
  isLoading: boolean = true;
  searchTerm: string = '';
  selectedStoreId: number | null = null;
  stores: Store[] = [];

  
storeForm = new FormGroup({
  name: new FormControl('', Validators.required),
  phoneNumber: new FormControl('', Validators.required),
  email: new FormControl('', [Validators.required, Validators.email]),
  address: new FormGroup({
    street: new FormControl('', Validators.required),
    locality: new FormControl('', Validators.required),
    city: new FormControl('Bogotá', Validators.required)
  })
});
  
  
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

  openModal(){
    this.isEditing = false;
    //Limpia el form
    this.storeForm.reset({
      address: { city: 'Bogotá' }
      });
    this.isModalOpen = true;
  }

  closeModal(){
    this.isModalOpen = false;
  }

  //EDITAR ---------------------------------------------------------

  openedEditModal(store: Store){

    this.isEditing = true;
    this.selectedStoreId = store.id;
    this.isModalOpen = true;

    // CARGA LOS DATOS EN EL FORM
    this.storeForm.patchValue({
      name: store.name,
      phoneNumber: store.phoneNumber,
      email: store.email,
      address: {
        street: store.address.street,
        locality: store.address.locality,
        city: store.address.city
      }
    });
  }

//GUARDAR ---------------------------------------------------------

saveStore() {
  if (this.storeForm.invalid) return;

  const storeData = this.storeForm.value;

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
