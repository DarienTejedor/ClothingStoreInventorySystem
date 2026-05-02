import { Component, inject } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';


@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.css'
})
export class MainLayoutComponent {
  
  private router = inject(Router);
  
  userRole = localStorage.getItem('role') || '';
  userName = localStorage.getItem('name') || '';

  isSidebarOpen = true;

  toggleSidebar() {
    this.isSidebarOpen = !this.isSidebarOpen;
  }

  ngOnInit() {
  if (window.innerWidth < 768) {
    this.isSidebarOpen = false;
  }
}

  // 2. Definimos el menú con sus permisos
  menuOptions = [
    { label: 'Inicio', path: '/dashboard', roles: ['ROLE_GENERAL_ADMIN', 'ROLE_STORE_ADMIN', 'ROLE_CASHIER'] },
    { label: 'Usuarios', path: '/users', roles: ['ROLE_GENERAL_ADMIN'] },
    { label: 'Tiendas', path: '/stores', roles: ['ROLE_GENERAL_ADMIN'] },
    { label: 'Productos', path: '/products', roles: ['ROLE_GENERAL_ADMIN', 'ROLE_STORE_ADMIN'] },
    { label: 'Ventas', path: '/sales', roles: ['ROLE_GENERAL_ADMIN', 'ROLE_STORE_ADMIN', 'ROLE_CASHIER'] }
  ];

  // 3. Función que decide si se muestra o no el ítem

  filteredMenu = this.menuOptions.filter(option => option.roles.includes(this.userRole));

  logout(): void {
    if (confirm('¿Estás seguro de que quieres cerrar sesión?')) {
      // Limpiamos todo el almacenamiento
      localStorage.clear();
      // Redirigimos al login
      this.router.navigate(['/login']);
    }
  }

}