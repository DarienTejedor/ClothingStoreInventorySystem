import { inject } from "@angular/core";
import { CanActivateFn, Router } from "@angular/router";
import { isTokenExpired } from "../jwt.decode";


export const authGuard: CanActivateFn = (route, state) => {
    const router = inject(Router);
    const token = sessionStorage.getItem('token');

    if (!token || isTokenExpired(token)) {
        sessionStorage.clear();
        router.navigate(['/login']);
        return false;
    }

    return true;
}