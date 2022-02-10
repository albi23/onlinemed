import {Injectable} from '@angular/core';
import {Person} from '../../core/sdk/onlinemed-model';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  static createSession(token: string, person: Person): void {
    SessionService.setToken(token);
    SessionService.setPerson(person);
  }

  static setToken(token: string): void {
    localStorage.setItem('sessionToken', token);
  }

  static setPerson(person: Person): void {
    localStorage.setItem('loggedPerson', JSON.stringify(person));
  }

  static getPerson(): Person {
    const person = localStorage.getItem('loggedPerson');
    return person ? JSON.parse(person) : {};
  }

  static getToken(): string {
    const token = localStorage.getItem('sessionToken');
    return token ? token : '';
  }

  static clearSession(): void {
    localStorage.setItem('loggedPerson', '');
    localStorage.setItem('sessionToken', '');
    localStorage.clear();
  }
}
