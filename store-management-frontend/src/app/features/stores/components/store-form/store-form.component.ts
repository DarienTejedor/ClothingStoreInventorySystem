import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Store } from '../../../../core/models/store.model';

@Component({
  selector: 'app-store-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './store-form.component.html',
  styleUrl: './store-form.component.css',
})
export class StoreFormComponent {

  @Input() isEditing = false;
  @Input() isOpen = false;
  
  // Eventos para avisar al padre
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<any>();

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
// Este método lo llamará el PADRE usando una referencia (ViewChild) o mediante cambios de Input
  setData(store: Store) {
    this.storeForm.patchValue(store);
  }

  resetForm() {
    this.storeForm.reset({ address: { city: 'Bogotá' } });
  }

  onSave() {
    if (this.storeForm.valid) {
      this.save.emit(this.storeForm.value);
    }
  }

  onClose() {
    this.close.emit();
  }

}
