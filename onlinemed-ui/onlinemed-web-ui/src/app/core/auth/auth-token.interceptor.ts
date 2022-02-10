import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {AuthService} from './auth.service';
import {map} from 'rxjs/operators';

@Injectable()
export class AuthTokenInterceptor implements HttpInterceptor {

  public static readonly TOKEN_HEADER = 'Token';

  constructor(private authService: AuthService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (this.authService.isAuthenticated()) {
      const clonedRequest = req.clone({
        headers: req.headers.set(AuthTokenInterceptor.TOKEN_HEADER,
          this.authService.getCurrentAuthToken())
      });
      return next.handle(clonedRequest).pipe(
        map(response => {
          if (response instanceof HttpResponse) {
            const newToken: string | null = response.headers.get(AuthTokenInterceptor.TOKEN_HEADER);
            if (newToken) {
              this.authService.setAuthToken(newToken);
            }
          }
          return response;
        })
      );
    }
    return next.handle(req);
  }
}
