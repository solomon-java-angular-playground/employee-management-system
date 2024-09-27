import { HttpInterceptorFn } from '@angular/common/http';
import {
  HttpRequest,
  HttpHandlerFn,
  HttpErrorResponse,
  HttpEvent,
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const JwtInterceptor: HttpInterceptorFn = (
  req: HttpRequest<any>,
  next: HttpHandlerFn
): Observable<HttpEvent<any>> => {
  const token = localStorage.getItem('token');
  const router = inject(Router);

  // Se il token è presente, clona la richiesta e aggiunge l'intestazione di autorizzazione
  if (token) {
    console.log('Found JWT token:', token);
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
  } else {
    console.warn('No JWT token found.');
  }

  // Passa la richiesta al prossimo gestore (handler) e intercetta eventuali errori
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        // Se il token è scaduto o non valido, reindirizza alla pagina di login
        console.error('Unauthorized request, redirecting to login.');
        localStorage.removeItem('token'); // Rimuove il token non valido
        router.navigate(['/login']); // Reindirizza alla pagina di login
      } else if (error.status === 403) {
        // Forbidden
        console.error('Access denied.');
      }
      return throwError(() => error); // Propaga l'errore per la gestione successiva
    })
  );
};
