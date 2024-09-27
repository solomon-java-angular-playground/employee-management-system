import { Routes, RouterModule } from '@angular/router';
import { EmployeeComponent } from './employee/employee.component';
import { EmployeeListComponent } from './employee-list/employee-list.component';
import { EmployeeResolver } from './employee-resolver';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { AuthGuard } from './auth/auth.guard';
import { DepartmentListComponent } from './department-list/department-list.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'employee/:employeeId', // Con l'ID dell'employee come parametro
    component: EmployeeComponent,
    resolve: { employee: EmployeeResolver },
    canActivate: [AuthGuard],
    data: { roles: ['HR'] }, // Solo gli utenti con ruolo HR possono accedere
  },
  {
    path: 'employee', // Rotta per il caso senza ID
    component: EmployeeComponent,
    canActivate: [AuthGuard],
    data: { roles: ['HR'] }, // Solo gli utenti con ruolo HR possono accedere
  },
  {
    path: 'employee-list',
    component: EmployeeListComponent,
    canActivate: [AuthGuard],
    data: { roles: ['HR'] },
  },
  {
    path: 'department-list',
    component: DepartmentListComponent,
    canActivate: [AuthGuard],
    data: { roles: ['USER', 'HR'] }, // Accessibile a USER e HR
  },
  {
    path: '',
    component: EmployeeListComponent,
    canActivate: [AuthGuard],
    data: { roles: ['HR'] },
  },
  { path: '**', redirectTo: 'department-list' }, // Route di fallback
];
