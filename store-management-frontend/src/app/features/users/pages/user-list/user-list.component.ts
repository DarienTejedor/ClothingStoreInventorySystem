import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { UserResponse } from '../../model/user.model';
import { UserFormComponent } from "../../components/user-form/user-form.component";
import { finalize } from 'rxjs';

@Component({
  selector: 'app-user-list',
  standalone: true,
  imports: [CommonModule, UserFormComponent, UserFormComponent], // Agrega aquí ReactiveFormsModule o subcomponentes si los usas
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
  
  rolesList: any[] = [];   
  storesList: any[] = [];  
  
  
  constructor(
    private userService: UserService,
    private cdr: ChangeDetectorRef
  ) {}
  
  onSearch(event: any): void {
      const value = event.target.value;
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
    this.userService.getRoles().subscribe({
      next: (response: any) => {
        this.rolesList = response.content || [];
        this.cdr.detectChanges();
      },
      error: (e) => console.error('Error al cargar roles para el form', e)});

    // Cargamos las tiendas del back (Usa tu userService o storeService aquí)
    this.userService.getStores().subscribe({
      next: (response: any) => {
        this.storesList = response.content || [];
        this.cdr.detectChanges();
      },
      error: (e) => console.error('Error al cargar tiendas para el form', e)
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

  handleSave(userData: any) {
    if (this.isEditing && this.selectedUserId) {
      this.userService.updateUser(this.selectedUserId, userData).subscribe({
        next: () => {
          alert('Usuario actualizado');
          this.finishProcess();
        }
      });
    } else {
      this.userService.createUser(userData).subscribe({
        next: () => {
          alert('Usuario creado');
          this.finishProcess();
        }
      });
    }
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
      next: (response: any) => {
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
  deleteUser(id: number | undefined): void {
    if (!id) return; 

    if (confirm('¿Estás seguro de que deseas eliminar este usuario?')) {
      this.userService.deleteUser(id).subscribe({
        next: () => {
          this.loadUsers(); 
        },
        error: (e) => console.error('Error al borrar usuario:', e)
      });
    }
  }}

