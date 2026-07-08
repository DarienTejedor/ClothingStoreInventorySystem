import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { HomeComponent } from './features/dashboard/pages/home/home.component';
import { authGuard } from './core/guards/auth.guard';
import { MainLayoutComponent } from './core/layouts/main-layout/main-layout.component';
import { StoreListComponent } from './features/stores/pages/store-list/store-list.component';
import { UserListComponent } from './features/users/pages/user-list/user-list.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent},

    { 
        path: '', 
        component: MainLayoutComponent,
        canActivate: [authGuard],
        children: [
            { path: 'dashboard', component: HomeComponent},
            { path: 'users', component: UserListComponent},
            { path: 'stores', component: StoreListComponent},
            { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
        ]
    },

    { path: '**', redirectTo: 'login', pathMatch: 'full'}
];
