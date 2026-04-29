import { ChangeDetectorRef, Component } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { AuthRequest } from "../../../../core/models/auth.model";
import { AuthService } from "../../../../core/services/auth.service";
import { Router } from "@angular/router";

@Component({
    selector: 'app-login',          
    standalone: true,               
    imports: [FormsModule],                     
    templateUrl: './login.component.html', 
    styleUrl: './login.component.css',
})
export class LoginComponent{
    errorMessage: string = '';
    loginData: AuthRequest = {
        loginUser: '',
        password: ''
    }
    isLoading: boolean = false;

    constructor(
        private authService: AuthService,
        private router: Router,
        private cdr: ChangeDetectorRef
    ) {}


    onLogin(){
        this.isLoading = true; 
        this.errorMessage = '';

        this.authService.login(this.loginData).subscribe({
            next: (response) => {
                localStorage.setItem('token', response.token);
                //PRUEBAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                localStorage.setItem('role', 'CASHIER')
                //
                console.log('Login exitoso', response);
                this.router.navigate(['/dashboard']);
            },
            error: (err) => {
                console.error(err)
                this.errorMessage = 'Usuario o contraseña incorrectos. Intenta nuevamente.';
                this.isLoading = false;
                this.cdr.detectChanges();
            } 
        })
    }
}