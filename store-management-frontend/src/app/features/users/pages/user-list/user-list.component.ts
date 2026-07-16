import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { UserResponse } from '../../model/user.model';
import { UserFormComponent } from "../../components/user-form/user-form.component";
import { finalize } from 'rxjs';
import { AlertService } from '../../../../shared/services/alert.service';
import { Store, StorePageResponse } from '../../../stores/models/store.model';
import { UserFormEvent } from '../../model/user-form-event.model';
import { UserPageResponse } from '../../model/user-page.model';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, UserFormComponent], 
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  @ViewChild(UserFormComponent) userFormChild!: UserFormComponent;
  users: UserResponse[] = [];
  isLoading: boolean = false;
  isModalOpen: boolean = false;
  isEditing: boolean = false;
  searchTerm: string = '';
  selectedUserId: number | null = null;
  currentRole = sessionStorage.getItem('role') || '';
  currentUserId = Number(sessionStorage.getItem('id'));
  
  rolesList: any[] = [];   
  storesList: Store[] = [];  
  
  constructor(
    private userService: UserService,
    private alertService: AlertService,
    private cdr: ChangeDetectorRef
  ) {}
  
  onSearch(event: Event): void {
      const value = (event.target as HTMLInputElement).value;

      this.searchTerm = value;
      this.loadUsers(this.searchTerm);
  }

  offLoading(){
    this.cdr.detectChanges();
    return this.isLoading = false;
  }

  ngOnInit(): void {
    this.loadUsers();
    this.loadData();
  }

  loadData(): void {
    // Cargamos los roles del back
    if (this.currentRole === 'ROLE_GENERAL_ADMIN') {
      this.userService.getAssignableRoles().subscribe({
        next: (response: any) => {
          this.rolesList = (response.content || []).filter((role: any) => role.name !== 'ROLE_GENERAL_ADMIN'); // Filtra el GA asi no sale como opcion en el form
          this.cdr.detectChanges();
        },
        error: (e) => console.error('Error al cargar roles para el form', e)});

      // Cargamos las tiendas del back (Usa tu userService o storeService aquí)
      this.userService.getStores().subscribe({
        next: (response: StorePageResponse) => {
          this.storesList = response.content || [];
          this.cdr.detectChanges();
        },
        error: (e) => console.error('Error al cargar tiendas para el form', e)
      });
    }
  }

  // Función para determinar si el usuario puede editar otro usuario
  canEdit(user: UserResponse): boolean {
  if (this.currentRole === 'ROLE_GENERAL_ADMIN') {
    return true;
  }
  if (this.currentRole === 'ROLE_STORE_ADMIN') {
    return user.roleName !== 'GENERAL_ADMIN';
  }
    return false;
  }

  // Función para determinar si el usuario puede eliminar otro usuario
  canDelete(user: UserResponse): boolean {
  if (user.roleName === 'GENERAL_ADMIN') {
    return false;
  }
  if (this.currentRole === 'ROLE_GENERAL_ADMIN') {
    return true;
  }
  if (this.currentRole === 'ROLE_STORE_ADMIN') {
    return user.id !== this.currentUserId;
  }
    return false;
  }

  // Función para determinar si el usuario puede resetear la contraseña de otro usuario
  canResetPassword(user: UserResponse): boolean {
  if (user.id === this.currentUserId) {
    return false;
  }
  if (this.currentRole === 'ROLE_GENERAL_ADMIN') {
    return user.roleName !== 'GENERAL_ADMIN';
  }
  if (this.currentRole === 'ROLE_STORE_ADMIN') {
    return user.roleName === 'CASHIER';
  }
    return false;
  }

  // Función para resetear la contraseña de un usuario
  async resetPassword(user: UserResponse): Promise<void> {
  const result = await this.alertService.confirmResetPassword(user.name);
  if (!result.isConfirmed) {
    return;
  }
  this.userService.resetPassword(user.id).subscribe({
    next: (response) => {
      this.alertService.showTemporaryPassword(
        response.temporaryPassword
      );
    },
    error: (e) => console.error(e)
    });
  }

  //MODAL ---------------------------------------------------------

  openModal(): void {
    this.isEditing = false;
    this.isModalOpen = true;
    this.userFormChild.resetForm(); // Le decimos al hijo que limpie
  }

  //EDITAR ---------------------------------------------------------
  
  openedEditModal(user: UserResponse): void {
    this.isEditing = true;
    this.selectedUserId = user.id;
    this.isModalOpen = true;
    // Le pasamos los datos al hijo
    setTimeout(() => this.userFormChild.setData(user), 0);
  }

  handleSave(event: UserFormEvent){
  // CREAR USUARIO
  if (!this.isEditing) {

    if (event.type !== 'CREATE') {return;}

    this.userService.createUser(event.data).subscribe({
      next: () => {
        this.alertService.success('Usuario creado con éxito');
        this.finishProcess();
      }
    });
    return;
  }

  if (!this.selectedUserId) return;

  switch (event.type) {

    case 'INFO':
      this.userService
        .updateUserInfo(this.selectedUserId, event.data)
        .subscribe({
          next: () => {
            this.alertService.success('Información actualizada');
            this.refreshData();
          }
        });
      break;

    case 'PASSWORD':
      this.userService
        .updateUserPassword(this.selectedUserId, event.data)
        .subscribe({
          next: () => {
            this.alertService.success('Contraseña actualizada');
            this.refreshData();
          }
        });
      break;

    case 'PERMISSIONS':
      this.userService
        .updateUserPermissions(this.selectedUserId, event.data)
        .subscribe({
          next: () => {
            this.alertService.success('Permisos actualizados');
            this.refreshData();
          }
        });
      break;
    }
  }

  private refreshData() {
    this.loadUsers();
  }
  
  private finishProcess() {
      this.isModalOpen = false;
      this.isEditing = false;
      this.selectedUserId = null;
      this.loadUsers();
    }
  
  // CARGA DE USUARIOS ---------------------------------------------------------
  loadUsers(term: string = ''): void {
    this.isLoading = true;
    
    this.userService.getUsers(term)
    .pipe(
      finalize(() => this.offLoading())
    ).subscribe({
      next: (response: UserPageResponse) => {
        this.users = response.content;
        this.offLoading();
      },
      error: (e) => {
        console.error('Error al cargar usuarios:', e);
        this.offLoading();
      }
    });
  }

  // ELIMINAR ---------------------------------------------------------
   async deleteUser(id: number | undefined): Promise<void> {
    if (!id) return; 

    const result = await this.alertService.confirmDelete('usuario');

    if (result.isConfirmed) {
      this.userService.deleteUser(id).subscribe({
        next: () => {
          this.loadUsers(); 
        },
        error: (e) => console.error('Error al borrar usuario:', e)
      });
    }
  }}

