import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { JwtHelperService, JWT_OPTIONS } from '@auth0/angular-jwt';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private loginUrl = 'http://localhost:9090/login';
  private registerUrl = 'http://localhost:9090/register';

  public jwtHelper: JwtHelperService = new JwtHelperService();

  constructor(private http: HttpClient, private router: Router) {}

  // Effettua il login e memorizza il token JWT
  login(username: string, password: string): Observable<string> {
    return this.http
      .post<string>(
        this.loginUrl,
        { username, password },
        { responseType: 'text' as 'json' }
      )
      .pipe(
        tap((token) => {
          localStorage.setItem('token', token); // Memorizza il token nel localStorage
          const userRoles = this.getUserRoles();
          if (!userRoles.includes('HR')) {
            this.router.navigate(['/department-list']); // Reindirizza se l'utente non è HR
          } else {
            this.router.navigate(['/employee-list']);
          }
        })
      );
  }

  // Registra l'utente e memorizza il token JWT, se viene restituito
  register(username: string, password: string): Observable<string> {
    return this.http
      .post<string>(
        this.registerUrl,
        { username, password },
        { responseType: 'text' as 'json' }
      )
      .pipe(
        tap((token) => {
          localStorage.setItem('token', token); // Memorizza il token se viene restituito
        })
      );
  }

  // Recupera il token dal localStorage
  getToken(): string | null {
    if (typeof localStorage !== 'undefined') {
      return localStorage.getItem('token');
    }
    return null;
  }

  // Decodifica il token e ritorna i ruoli dell'utente
  getUserRoles(): string[] {
    const token = this.getToken();
    if (token && !this.jwtHelper.isTokenExpired(token)) {
      const decodedToken = this.jwtHelper.decodeToken(token);
      return decodedToken.roles || [];
    }
    return [];
  }

  // Verifica se l'utente ha un ruolo specifico
  hasRole(role: string): boolean {
    return this.getUserRoles().includes(role);
  }

  // Verifica se l'utente è loggato
  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }

  // Effettua il logout rimuovendo il token dal localStorage
  logout(): void {
    localStorage.removeItem('token');
  }
}
