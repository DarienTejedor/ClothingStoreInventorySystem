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


  infoForm = new FormGroup({
  loginUser: new FormControl(''),
  name: new FormControl('', Validators.required),
  document: new FormControl('', [
    Validators.required,
    Validators.pattern('^[0-9]+$')
    ])
  });

  passwordForm = new FormGroup({
    oldPassword: new FormControl('', Validators.required),
    newPassword: new FormControl('', [
      Validators.required,
      Validators.minLength(6)
    ]),
    confirmPassword: new FormControl('', Validators.required)
  });

  permissionsForm = new FormGroup({
    roleId: new FormControl<number | string>('', Validators.required),
    storeId: new FormControl<number | string>('', Validators.required)
  });

  // Este método lo llamará el PADRE (user-list) al presionar "Editar"
  setData(user: UserResponse) {
    this.isGeneralAdmin = user.roleName === 'GENERAL_ADMIN'; // Actualizamos la variable según el rol del usuario
    if (this.isGeneralAdmin) {
      this.permissionsForm.get('roleId')?.disable();
      this.permissionsForm.get('storeId')?.disable();
    } else {
      this.permissionsForm.get('roleId')?.enable();
      this.permissionsForm.get('storeId')?.enable();
    }
    this.infoForm.patchValue({
        loginUser: user.loginUser,
        name: user.name,
        document: user.document.toString() || '',
    })
    this.permissionsForm.patchValue({
        roleId: user.roleId,
        storeId: user.storeId
    });
  }

  // Este método lo llamará el PADRE al presionar "Nuevo Usuario"
  resetForm() {
    this.infoForm.reset();
    this.passwordForm.reset();
    this.permissionsForm.reset();
    // Reestablecemos el validador requerido para nuevos registros
    this.isGeneralAdmin = false; // Reseteamos la variable al crear un nuevo usuario
    this.permissionsForm.patchValue({
      roleId: '',
      storeId: ''
    });
  }

  onClose() {
    this.close.emit();
  }
  onSaveInfo() {
    if (this.infoForm.valid) {
      // Puedes enviar un objeto indicando qué se actualizó
      this.save.emit({ type: 'INFO', data: this.infoForm.value });
    }
  }

  onSavePassword() {
    if (this.passwordForm.valid) {
      this.save.emit({ type: 'PASSWORD', data: this.passwordForm.value });
    }
  }

  onSavePermissions() {
    if (this.permissionsForm.valid) {
      // Usamos getRawValue() por si los campos están deshabilitados (isGeneralAdmin)
      this.save.emit({ type: 'PERMISSIONS', data: this.permissionsForm.getRawValue() });
    }
  }
}