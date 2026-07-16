import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { isTokenExpired } from "../jwt.decode";
import { AuthService } from "../services/auth.service";


export const authGuard: CanActivateFn = (route, state) => {
    const router = inject(Router);
    const authService = inject(AuthService);
    const token = authService.getToken();

    if (!token || isTokenExpired(token)) {
        authService.logout();
        router.navigate(['/login']);
        return false;
    }

    return true;
}