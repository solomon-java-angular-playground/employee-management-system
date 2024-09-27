import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app.component';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import { JwtInterceptor } from './app/service/jwt-interceptor.service';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';

bootstrapApplication(AppComponent, {
  ...appConfig,
  providers: [
    provideHttpClient(withInterceptors([JwtInterceptor])),
    JwtHelperService,
    ...appConfig.providers,
    provideAnimationsAsync(),
  ],
}).catch((err) => console.error(err));
