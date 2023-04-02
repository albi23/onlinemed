import {EventEmitter, Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {ErrorHandlerService} from '../sdk/error-handler.service';
import {map, take} from 'rxjs/operators';
import {SessionService} from '../../shared/services/session.service';
import {Functionality, Person} from '../sdk/onlinemed-model';
import {Utility} from '../../shared/utilities/utility';
import {Router} from '@angular/router';
import {TranslateService} from '../translations/translate.service';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authToken: string;
  private loggedUser: Person;
  private currentPersonFunctionalities: Functionality[] = [];
  private logInEvent: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor(private httpService: HttpClient,
              private errorHandlerService: ErrorHandlerService,
              private translateService: TranslateService,
              private router: Router) {
  }

  onLogin(username: string, password: string): void {

    this.login(username, password).subscribe((response) => {
        SessionService.clearSession();
        if (response.token) {
          this.setAuthToken(response.token);
          this.setLoggedPerson(response.person);
          this.extractPersonFunctionalities(response.person);
        }
        this.logInEvent.emit(true);
        this.router.navigate(['/om']);
      },
      (err) => {
        Utility.showViolationsIfOccurs(err);
        this.logInEvent.emit(false);
      }
    );
  }

  private login(username: string, password: string): Observable<{ [key: string]: any }> {
    let headers = new HttpHeaders().set('Content-type', 'application/json');
    headers = headers.append('Authorization', 'Basic ' + btoa(username + ':' + password));
    const subject = new Subject<{ [key: string]: any }>();
    this.httpService.post(environment.BASE_URL + '/api/login', null, {headers, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  setAuthToken(token: string): void {
    SessionService.setToken(token);
    this.authToken = token;
  }

  setLoggedPerson(person: Person): void {
    SessionService.setPerson(person);
    this.loggedUser = person;
  }

  sessionLogin(): void {
    this.saveSessionInApp();
    this.functionalitiesInit();
  }

  saveSessionInApp(): void {
    this.authToken = SessionService.getToken();
    this.loggedUser = SessionService.getPerson();
  }

  functionalitiesInit(): void {
    this.extractPersonFunctionalities(this.getCurrentLoggedPerson());
    this.setCurrentFunctionality();
    this.updateDefaultLanguageForPerson();
  }

  isAuthenticated(): boolean {
    const token = SessionService.getToken();
    const person = SessionService.getPerson();
    return token !== '' && !!person.id;
  }


  getCurrentAuthToken(): string {
    return this.authToken ? this.authToken : SessionService.getToken();
  }

  public getCurrentLoggedPerson(): Person {
    return this.loggedUser ? this.loggedUser : SessionService.getPerson();
  }

  private extractPersonFunctionalities(person: Person): void {
    const functionalitiesSet: Set<Functionality> = new Set<Functionality>();
    person.roles.flatMap(r => r.functionalities)
      .filter(f => f.name) // proxy objects => !undefined
      .forEach(f => functionalitiesSet.add(f));
    this.currentPersonFunctionalities = [...functionalitiesSet];
  }

  public getCurrentLoggedPersonFunctionalities(): Functionality[] {
    return this.currentPersonFunctionalities;
  }

  public getLogInStatus(): EventEmitter<boolean> {
    return this.logInEvent;
  }

  private setCurrentFunctionality(): void {
    if (this.getCurrentLoggedPerson().roles &&
      this.getCurrentLoggedPerson().roles[0] &&
      this.getCurrentLoggedPersonFunctionalities[0]) {
      // this.currentFunctionality = this.getCurrentPersonRoles()[0].functionalities[0];
      this.translateService.addFunctionalities(this.getCurrentLoggedPersonFunctionalities[0].name);
    }
  }

  updateDefaultLanguageForPerson(): void {
    if (this.translateService.getCurrentLanguage() &&
      this.translateService.getCurrentLanguage().locale !== this.getCurrentLoggedPerson().defaultLanguage) {
      this.translateService.setLanguageForLocale(this.getCurrentLoggedPerson().defaultLanguage);
    }
  }

  logout(): void {
    this.authToken = '';
    SessionService.clearSession();
    this.router.navigate(['/login']);
  }
}
