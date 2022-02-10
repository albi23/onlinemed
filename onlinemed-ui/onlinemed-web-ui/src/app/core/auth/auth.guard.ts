import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot} from '@angular/router';
import {Observable, Subscriber} from 'rxjs';
import {TranslateService} from '../translations/translate.service';
import {AuthService} from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanActivateChild {

  constructor(private router: Router,
              private authService: AuthService,
              private translateService: TranslateService) {
  }


  canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean>{
    return this.canActivate(route, state);
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return new Observable<boolean>((observer) => {
      if (this.translateService.currentLanguage) {
        this.roadCheck(state, observer);
      } else {
        this.translateService.languagesLoaded
          .subscribe(() => {
            this.roadCheck(state, observer);
          });
        this.translateService.loadLanguages();
      }
    });
  }
  private roadCheck(state: RouterStateSnapshot, observer: Subscriber<boolean>): void {
    if (this.authService.isAuthenticated()) {
      this.authService.sessionLogin();
      observer.next(true);
    } else {
      if (state.url === '/om') {
        this.router.navigate(['/login']);
      }
      observer.next(false);
    }
  }

}
