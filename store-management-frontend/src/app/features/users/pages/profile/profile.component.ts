import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { UserResponse } from '../../model/user.model';
import { AlertService } from '../../../../shared/services/alert.service';
import { AuthService } from '../../../../core/services/auth.service';
import { UserFormFactory } from '../../../../shared/forms/user-form.factory';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {

  user: UserResponse | null = null;
  isLoading = true;

  infoForm = new FormGroup({
    loginUser: new FormControl(''),
    name: new FormControl('', Validators.required),
    document: new FormControl('', [
      Validators.required,
      Validators.pattern('^[0-9]+$')
    ]),
    roleName: new FormControl(''),
    storeName: new FormControl('')
  });

  passwordForm = UserFormFactory.createPasswordForm();

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private alertService: AlertService,
  ) {}

  ngOnInit(): void {
    const id = Number(this.authService.getUserId());

    if (id) {
      this.loadUser(id);
    } else {
      this.isLoading = false;
    }
  }

  loadUser(id: number): void {
    this.userService.getUserById(id).subscribe({
      next: (user) => {
        this.user = user;

        this.infoForm.patchValue({
          loginUser: user.loginUser,
          name: user.name,
          document: user.document.toString(),
          roleName: user.roleName,
          storeName: user.storeName
        });

        this.isLoading = false;
      },
      error: (e) => {
        console.error(e);
        this.alertService.error('No se pudo cargar el perfil');
        this.isLoading = false;
      }
    });
  }

  saveInfo(): void {
    if (!this.user || this.infoForm.invalid) return;

    const payload = {
      name: this.infoForm.value.name!,
      document: Number(this.infoForm.value.document)
    };

    this.userService.updateUserInfo(this.user.id, payload).subscribe({
      next: () => {
        this.alertService.success('Información actualizada');
      },
      error: (e) => {
        console.error(e);
        this.alertService.error('No se pudo actualizar la información');
      }
    });
  }

  savePassword(): void {
    if (!this.user || this.passwordForm.invalid) return;

    if (this.passwordForm.value.newPassword !== this.passwordForm.value.confirmPassword) {
      this.alertService.warning('Las contraseñas no coinciden');
      return;
    }

    const payload = {
      oldPassword: this.passwordForm.value.oldPassword!,
      newPassword: this.passwordForm.value.newPassword!
    };

    this.userService.updateUserPassword(this.user.id, payload).subscribe({
      next: () => {
        this.alertService.success('Contraseña actualizada');
        this.passwordForm.reset();
      },
      error: (e) => {
        console.error(e);
        this.alertService.error('No se pudo actualizar la contraseña');
      }
    });
  }
}