import { Component, HostListener, inject } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AlertService } from '../../../shared/services/alert.service';
import { AuthService } from '../../services/auth.service';


@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.css'
})
export class MainLayoutComponent {
  
  private router = inject(Router);
  private alertService = inject(AlertService);
  private authService = inject(AuthService);
  isUserMenuOpen = false;
  

  userRole = this.authService.getRole() || '';
  userName = this.authService.getName() || '';

  isSidebarOpen = true;


  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  ngOnInit() {
  if (window.innerWidth < 768) {
    this.isSidebarOpen = false;
    }
  }

  toggleUserMenu() {
    this.isUserMenuOpen = !this.isUserMenuOpen;
  }

  goToProfile() {
    this.router.navigate(['/profile']);
    this.isUserMenuOpen = false;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent) {
    if (!(event.target as HTMLElement).closest('.user-menu')) {
      this.isUserMenuOpen = false;
    }
  }

  //Mejorar la presentacion del rol del usuario en el menú, mostrando un nombre más amigable
  get roleLabel(): string {
  switch (this.userRole) {
    case 'ROLE_GENERAL_ADMIN':
      return 'Administrador General';
    case 'ROLE_STORE_ADMIN':
      return 'Administrador de Tienda';
    case 'ROLE_CASHIER':
      return 'Cajero';
    default:
      return '';
    }
  }

  // 2. Definimos el menú con sus permisos
  menuOptions = [
    { label: 'Inicio', path: '/dashboard', roles: ['ROLE_GENERAL_ADMIN', 'ROLE_STORE_ADMIN', 'ROLE_CASHIER'] },
    { label: 'Usuarios', path: '/users', roles: ['ROLE_GENERAL_ADMIN', 'ROLE_STORE_ADMIN'] },
    { label: 'Tiendas', path: '/stores', roles: ['ROLE_GENERAL_ADMIN'] },
    { label: 'Productos', path: '/products', roles: ['ROLE_GENERAL_ADMIN', 'ROLE_STORE_ADMIN'] },
    { label: 'Ventas', path: '/sales', roles: ['ROLE_GENERAL_ADMIN', 'ROLE_STORE_ADMIN', 'ROLE_CASHIER'] }
  ];

  // 3. Función que decide si se muestra o no el ítem

  get filteredMenu() {
  return this.menuOptions.filter(option =>
      option.roles.includes(this.userRole)
    );
  }

  async logout(): Promise<void> {

    const result = await this.alertService.confirmLogout();

    if (result.isConfirmed) {
      // Limpiamos todo el almacenamiento
      this.authService.logout();
      // Redirigimos al login
      this.router.navigate(['/login']);
    }
  }

}