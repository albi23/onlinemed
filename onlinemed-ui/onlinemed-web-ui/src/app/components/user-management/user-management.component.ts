import {Component, OnInit, ViewChild} from '@angular/core';
import {UserManagementTableComponent} from './user-managment-table';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styles: []
})
export class UserManagementComponent implements OnInit {

  @ViewChild('usersTableComponent') selectableItems: UserManagementTableComponent;
  spinnerOn = false;
  constructor() { }

  ngOnInit(): void {
  }

  setSpinnerStatus(value: any): void {
    this.spinnerOn = (value as boolean);
  }
}
