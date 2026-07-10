import { Injectable } from '@angular/core';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  success(message: string) {
    return Swal.fire({
      icon: 'success',
      title: 'Éxito',
      text: message,
      confirmButtonColor: '#C97A8C'
    });
  }

  error(message: string) {
    return Swal.fire({
      icon: 'error',
      title: 'Error',
      text: message,
      confirmButtonColor: '#C97A8C'
    });
  }

  warning(message: string) {
    return Swal.fire({
      icon: 'warning',
      title: 'Atención',
      text: message,
      confirmButtonColor: '#C97A8C'
    });
  }

  confirmDelete(item: string) {
    return Swal.fire({
      title: `¿Eliminar ${item}?`,
      text: 'Esta acción no se puede deshacer.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#C97A8C',
      cancelButtonColor: '#A85A6E',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    });
  }

  confirmLogout() {
    return Swal.fire({
      title: 'Cerrar sesión',
      text: '¿Deseas cerrar la sesión?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#C97A8C',
      cancelButtonColor: '#A85A6E',
      confirmButtonText: 'Cerrar sesión',
      cancelButtonText: 'Cancelar'
    });
  }
}