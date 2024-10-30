import { Component } from '@angular/core';
import { RouterLink, Router } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../auth/auth.service';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { HttpClient } from '@angular/common/http';
import { saveAs } from 'file-saver'; // Per il download dei file (report)

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    MatToolbarModule,
    RouterLink,
    MatButtonModule,
    CommonModule,
    MatIconModule,
    MatMenuModule,
  ],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent {
  constructor(public authService: AuthService, private router: Router, private http: HttpClient) { }

  ngOnInit(): void { }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  // Funzione per scaricare il report degli impiegati
  downloadEmployeeReport(): void {
    this.http.get('/api/download/employee-report', { responseType: 'blob' })
      .subscribe((data: Blob) => {
        // Utilizza la libreria file-saver per scaricare il file CSV
        saveAs(data, 'employee_report.csv');
      }, error => {
        console.error('Error downloading the employee report', error);
      });
  }
}
