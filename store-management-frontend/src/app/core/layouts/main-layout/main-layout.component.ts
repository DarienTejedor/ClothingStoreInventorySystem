import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';


@Component({
  selector: 'app-main-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './main-layout.component.html',
  styleUrl: './main-layout.component.css'
})
export class MainLayoutComponent {
// 1. Obtenemos el rol del almacenamiento
  userRole: string = localStorage.getItem('role') || '';

  // 2. Definimos el menú con sus permisos
  // Esto hace que el sistema sea MUTABLE para cualquier negocio
  menuOptions = [
    { label: 'Inicio', path: '/dashboard', roles: ['GENERAL_ADMIN', 'STORE_ADMIN', 'CASHIER'] },
    { label: 'Usuarios', path: '/users', roles: ['GENERAL_ADMIN'] },
    { label: 'Tiendas', path: '/stores', roles: ['GENERAL_ADMIN'] },
    { label: 'Productos', path: '/products', roles: ['GENERAL_ADMIN', 'STORE_ADMIN'] },
    { label: 'Ventas', path: '/sales', roles: ['GENERAL_ADMIN', 'STORE_ADMIN', 'CASHIER'] }
  ];

  // 3. Función que decide si se muestra o no el ítem
  canSee(rolesPermitidos: string[]): boolean {
    return rolesPermitidos.includes(this.userRole);
  }
}