import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SideBarService {

  private dataSource = new BehaviorSubject<boolean>(false);
  private data = this.dataSource.asObservable();
  private observers: BehaviorSubject<boolean>[] = [];

  constructor() {
  }

  registerObserver(observer: BehaviorSubject<boolean>): void{
    this.observers.push(observer);
  }

  updateState(state: boolean): void {
    this.observers.forEach(o => o.next(state));
    // this.dataSource.next(state);
  }

  getStateListeners(): Observable<boolean> {
    return this.data;
  }
}
