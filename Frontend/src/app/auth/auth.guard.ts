import { Injectable } from '@angular/core';
import {
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  UrlTree,
  Router,
} from '@angular/router';
import { AuthService } from './auth.service';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  private jwtHelper = new JwtHelperService();

  constructor(private authService: AuthService, private router: Router) {}

  // Controlla se il token esiste, non è scaduto
  // e se l'utente ha il ruolo richiesto per accedere alla rotta
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean | UrlTree {
    console.log('AuthGuard: checking access for route', state.url);

    if (this.authService.isLoggedIn()) {
      // Controlla se il token esiste e non è scaduto
      const token = localStorage.getItem('token');

      if (token && !this.jwtHelper.isTokenExpired(token)) {
        const tokenPayload = this.jwtHelper.decodeToken(token); // Decodifica il token
        const userRoles = tokenPayload?.roles || []; // Estrazione dei ruoli dal token come array

        // Controlla se la rotta richiede ruoli specifici
        const requiredRoles = route.data['roles'];

        // Verifica che l'utente abbia almeno uno dei ruoli richiesti
        const hasRequiredRole = requiredRoles.some((role: string) =>
          userRoles.includes(role)
        );

        if (!hasRequiredRole) {
          // Se l'utente non ha il ruolo richiesto, reindirizzalo a department-list
          this.router.navigate(['/department-list']);
          return false;
        }

        // Se l'utente ha il ruolo richiesto, permetti l'accesso
        return true;
      } else {
        // Se il token è scaduto o mancante, reindirizza al login
        return this.router.createUrlTree(['/login'], {
          queryParams: { returnUrl: state.url },
        });
      }
    } else {
      // Se l'utente non è autenticato, reindirizzalo al login
      return this.router.createUrlTree(['/login'], {
        queryParams: { returnUrl: state.url },
      });
    }
  }
}
