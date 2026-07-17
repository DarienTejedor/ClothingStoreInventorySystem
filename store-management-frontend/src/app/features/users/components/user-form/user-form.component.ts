import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserResponse } from '../../model/user.model';
import { UserFormFactory } from '../../../../shared/forms/user-form.factory';
import { Store } from '../../../stores/models/store.model';
import { UserFormEvent } from '../../model/user-form-event.model';
import { UserInfoRequest } from '../../model/user-info-request.model';
import { UserPasswordRequest } from '../../model/user-password-request.model';
import { UserPermissionsRequest } from '../../model/user-permissions-request.model';
import { UserData } from '../../model/user-request.model';
import { AuthService } from '../../../../core/services/auth.service';
import { UserRoleRequest } from '../../model/user-role-request.model';

@Component({
  selector: 'app-user-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css',
})
export class UserFormComponent {

  isGeneralAdmin: boolean = false; // Variable para controlar si el usuario es GENERAL_ADMIN
  isStoreAdmin: boolean = false; // Variable para controlar si el usuario es STORE_ADMIN
  isCurrentUser = false;

  @Input() isEditing = false;
  @Input() isOpen = false;
  
  @Input() rolesList: any[] = []; 
  @Input() storesList: Store[] = [];

  // Eventos para avisar al padre (user-list)
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<UserFormEvent>();

  constructor(
    private authService: AuthService
  ) {}


  infoForm = UserFormFactory.createInfoForm();

  passwordForm = UserFormFactory.createPasswordForm();

  permissionsForm = new FormGroup({
    roleId: new FormControl<number | null>(null, Validators.required),
    storeId: new FormControl<number | null>(null, Validators.required)
  });

  ngOnInit(): void {
    this.isGeneralAdmin = this.authService.isGeneralAdmin();
    this.isStoreAdmin = this.authService.isStoreAdmin();
  }

  // Este método lo llamará el PADRE (user-list) al presionar "Editar"
  setData(user: UserResponse) {

    this.isGeneralAdmin = user.roleName === 'GENERAL_ADMIN';

    if (this.isGeneralAdmin || this.isCurrentUser) {
      //El usuario editado es GA, deshabilitamos los campos de rol y tienda
      this.permissionsForm.get('roleId')?.disable();
      this.permissionsForm.get('storeId')?.disable();

    } else if (this.isStoreAdmin) {
      //SA solo puede cambiar el rol, no la tienda
      this.permissionsForm.get('roleId')?.enable();
      this.permissionsForm.get('storeId')?.disable();
    } else {
      //GA editando un usuario normal
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
    this.isGeneralAdmin = false; // Reseteamos la variable al crear un nuevo usuario

    this.permissionsForm.reset({
      roleId: null,
      storeId: this.isStoreAdmin
          ? this.authService.getStoreId()
          : null
    });

    if (this.isStoreAdmin) {
      this.permissionsForm.get('storeId')?.disable();
    } else {
      this.permissionsForm.get('storeId')?.enable();
    }
  }

  onClose() {
    this.close.emit();
  }
  
  onSaveInfo() {
    if (this.infoForm.invalid) return;

    const data: UserInfoRequest = {
      name: this.infoForm.getRawValue().name!,
      document: Number(this.infoForm.getRawValue().document)
    };

    this.save.emit({
      type: 'INFO',
      data
    });
  }

  onSavePassword() {
    if (this.passwordForm.invalid) return;

    const raw = this.passwordForm.getRawValue();

    const data: UserPasswordRequest = {
      oldPassword: raw.oldPassword!,
      newPassword: raw.newPassword!
    };

    this.save.emit({
      type: 'PASSWORD',
      data
    });
  }

  onSavePermissions() {
    if (this.permissionsForm.invalid) return;

    const raw = this.permissionsForm.getRawValue();

    const data: UserPermissionsRequest = {
      roleId: raw.roleId!,
      storeId: raw.storeId!
    };

    if (this.isStoreAdmin) {

      this.save.emit({
          type: 'ROLE',
          data: {
              roleId: raw.roleId!
          }
      });
      return;
    }

    this.save.emit({
      type: 'PERMISSIONS',
      data
    });
  }

  onCreateUser() {
  if (this.infoForm.invalid || this.permissionsForm.invalid) {
    return;
  }

  const info = this.infoForm.getRawValue();
  const permissions = this.permissionsForm.getRawValue();

  const data: UserData = {
    loginUser: info.loginUser!,
    name: info.name!,
    document: Number(info.document),
    roleId: permissions.roleId!,
    storeId: permissions.storeId!
  };

  this.save.emit({
    type: 'CREATE',
    data
  });
}
}