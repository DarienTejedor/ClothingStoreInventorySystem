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

  async confirmResetPassword(userName: string) {
  return Swal.fire({
    title: '¿Restablecer contraseña?',
    html: `
      Se generará una <b>contraseña temporal</b> para el usuario:
      <br><br>
      <b>${userName}</b>
      <br><br>
      El usuario deberá cambiarla al iniciar sesión.
    `,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: 'Sí, restablecer',
    cancelButtonText: 'Cancelar',
    confirmButtonColor: '#A85A6E',
    cancelButtonColor: '#9CA3AF'
    });
  }

async showTemporaryPassword(password: string) {
  await Swal.fire({
    title: 'Contraseña restablecida',
    icon: 'success',
    confirmButtonText: 'Aceptar',
    confirmButtonColor: '#A85A6E',
    html: `
      <p style="margin-bottom:14px;">
        La nueva contraseña temporal es:
      </p>

      <div
        id="copyPassword"
        title="Haz clic para copiar"
        style="position:relative; margin:18px 0; padding:16px 48px 16px 18px; border-radius:14px; border:1px solid #F4C0D1; background:#F9F5F2; cursor:pointer; transition:.2s;">
        <span style="font-size:20px; font-weight:600; font-family:monospace; letter-spacing:3px; color:#3D2E35; user-select:all;">
          ${password}
        </span>

        <div style="position:absolute; right:14px; top:50%; transform:translateY(-50%); color:#7A3A4E; display:flex; align-items:center;">
          <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16H6a2 2 0 01-2-2V6a2 2 0 012-2h8a2 2 0 012 2v2m-4 4h6a2 2 0 012 2v6a2 2 0 01-2 2h-6a2 2 0 01-2-2v-6a2 2 0 012-2z"/>
          </svg>
        </div>
      </div>

      <small id="helperText" style="color:#6B7280; transition: 0.2s;">
        Haz clic sobre la contraseña para copiarla.
      </small>
    `,

    didOpen: () => {
      const container = document.getElementById('copyPassword');
      const helperText = document.getElementById('helperText');

      container?.addEventListener('click', async () => {
        await navigator.clipboard.writeText(password);

        // Cambiamos el texto temporalmente para confirmar la acción
        if (helperText) {
          helperText.innerText = '¡Copiado en el portapapeles!';
          helperText.style.color = '#10B981'; // Color verde esmeralda
          helperText.style.fontWeight = '600';
        }

        // Lo restauramos después de 2 segundos
        setTimeout(() => {
          if (helperText) {
            helperText.innerText = 'Haz clic sobre la contraseña para copiarla.';
            helperText.style.color = '#6B7280';
            helperText.style.fontWeight = 'normal';
          }
        }, 2000);
      });
    }
  });
}
}