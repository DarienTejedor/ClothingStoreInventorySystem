import { HttpInterceptorFn } from "@angular/common/http";


export const authInterceptor: HttpInterceptorFn = (req, next) => {
    
    // No agregar token al login
    if (req.url.includes('/login')) {
        return next(req);
    }
    
    const token = localStorage.getItem('token');
    
    //Si tenemos un token se clona y se pone al header
    if(token){
        const clonedToken = req.clone({
            setHeaders: { Authorization: `Bearer ${token}`}
        });
        return next(clonedToken);
    }
    return next(req);
}