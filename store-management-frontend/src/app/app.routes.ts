import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { HomeComponent } from './features/dashboard/pages/home/home.component';
import { authGuard } from './core/guards/auth.guard';
import { MainLayoutComponent } from './core/layouts/main-layout/main-layout.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent},

    { 
        path: '', 
        component: MainLayoutComponent,
        canActivate: [authGuard],
        children: [
            // Aquí van los "hijos". El RouterOutlet del MainLayout los cargará
            { path: 'dashboard', component: HomeComponent },
            
            // Cuando se creen mas componentes, irán aquí:
            
            // Redirección inicial dentro del layout
            { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
        ]
    },

    { path: '', redirectTo: 'login', pathMatch: 'full'}
];
