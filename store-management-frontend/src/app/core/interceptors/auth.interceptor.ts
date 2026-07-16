import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';


export const authInterceptor: HttpInterceptorFn = (req, next) => {
    const router = inject(Router);
    const authService = inject(AuthService);

    // No agregar token al login ni al refresh
    if (req.url.includes('/login') || req.url.includes('/refresh')) {
        return next(req);
    }
    
    const token = authService.getToken();
    
    //Si tenemos un token se clona y se pone al header
    if(token){
        req = req.clone({
            setHeaders: {
                Authorization: `Bearer ${token}`
            }
        })
    }
    //Se envia la peticion
    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {

            // Si la api responde con el error es 403, no desloguea al usuario
            if (error.status === 403) {
                console.warn('Sin permisos para acceder a este recurso.');
                return throwError(() => error);
            }

            //si el Token ha expirado, intentamos renovarlo
            if (error.status === 401) {
                const refreshToken = authService.getRefreshToken();
                //Si el token es nulo, se limpia sesión y se redirige a login / desloguea al usuario
                if (!refreshToken) {
                    authService.logout();
                    return throwError(() => error);
                }
            
                //Si si existe se intenta renovar el token y luego reintentar la request original
                return authService.refreshToken(refreshToken).pipe(
                    switchMap((response) => {
                            // Guardamos el nuevo access token
                            authService.setToken(response.token);

                            // Reintentamos la request original con el nuevo token
                            const retryReq = req.clone({
                                setHeaders: { Authorization: `Bearer ${response.token}` }
                            });
                            return next(retryReq);
                    }),
                    catchError((refreshError) => {
                        // Si el refresh token también falla, limpiamos sesión y redirigimos a login
                        authService.logout();
                        router.navigate(['/login']);
                        return throwError(() => refreshError);
                    })
                );
            }

            return throwError(() => error); 
        })
    );
};