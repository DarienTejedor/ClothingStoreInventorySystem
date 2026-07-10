import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserResponse } from '../../model/user.model';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css',
})
export class UserFormComponent {

  isGeneralAdmin: boolean = false; // Variable para controlar si el usuario es GENERAL_ADMIN

  @Input() isEditing = false;
  @Input() isOpen = false;
  
  @Input() rolesList: any[] = []; 
  @Input() storesList: any[] = [];

  // Eventos para avisar al padre (user-list)
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<any>();


userForm = new FormGroup({
    loginUser: new FormControl('', [Validators.required, Validators.minLength(4)]),
    name: new FormControl('', Validators.required),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
    document: new FormControl('', [Validators.required, Validators.pattern('^[0-9]+$')]),
    roleId: new FormControl<number | string>('', Validators.required), 
    storeId: new FormControl<number | string>('', Validators.required)
  });

  // Este método lo llamará el PADRE (user-list) al presionar "Editar"
  setData(user: UserResponse) {
    this.isGeneralAdmin = user.roleName === 'GENERAL_ADMIN'; // Actualizamos la variable según el rol del usuario
    if (this.isGeneralAdmin) {

      this.userForm.get('roleId')?.disable();
      this.userForm.get('storeId')?.disable();

    } else {

      this.userForm.get('roleId')?.enable();
      this.userForm.get('storeId')?.enable();

    }
    this.userForm.patchValue({
        loginUser: user.loginUser,
        name: user.name,
        document: user.document.toString() || '',
        roleId: user.roleId,
        storeId: user.storeId
    });

    // Al editar, el password deja de ser obligatorio en el Front (el back no lo requerirá si no cambia)
    this.userForm.get('password')?.clearValidators();
    this.userForm.get('password')?.updateValueAndValidity();
  }

  // Este método lo llamará el PADRE al presionar "Nuevo Usuario"
  resetForm() {
    this.userForm.reset();
    // Reestablecemos el validador requerido para nuevos registros
    this.isGeneralAdmin = false; // Reseteamos la variable al crear un nuevo usuario
    this.userForm.patchValue({ roleId: '', storeId: '' });
    this.userForm.get('password')?.setValidators([Validators.required, Validators.minLength(6)]);
    this.userForm.get('password')?.updateValueAndValidity();
  }

  onSave() {
    if (this.userForm.valid) {
      this.save.emit(this.userForm.value);
    }
  }

  onClose() {
    this.close.emit();
  }
}