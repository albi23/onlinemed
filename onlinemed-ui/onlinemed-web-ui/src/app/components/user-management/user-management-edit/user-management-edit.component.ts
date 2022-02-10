import {Component, Inject, InjectionToken, Input, OnInit} from '@angular/core';
import {Person, Role} from '../../../core/sdk/onlinemed-model';
import {RoleType} from '../../../core/sdk/model-enums';
import {MultiDropdownAdapter} from '../../../shared/classes/multi-dropdown-adapter';

export class EditRoles {
  constructor(public nextItem: Person, public allRoles: Role[]) {
  }
}

export const OBJECT_TOKEN = new InjectionToken('obs');

@Component({
  selector: 'app-user-management-edit',
  templateUrl: './user-management-edit.component.html',
  styles: []
})
export class UserManagementEditComponent extends MultiDropdownAdapter implements OnInit {
  @Input() nextItem: Person;
  @Input() allRoles: Role[] = [];
  selectedItems: { id: number, name: string }[] = [];
  dropdownList: { id: number, name: string }[] = [];

  constructor(@Inject(OBJECT_TOKEN) public providedObject: EditRoles) {
    super();
    this.nextItem = providedObject.nextItem;
    this.allRoles = providedObject.allRoles;
  }


  ngOnInit(): void {
    this.dropdownList = this.mapToDropDownObject(this.allRoles);
    this.nextItem.roles.forEach(personRole => {
      const index = this.allRoles.findIndex(currentItemRole => currentItemRole.roleType === personRole.roleType);
      this.selectedItems.push({id: index, name: RoleType[personRole.roleType].toLowerCase()});
    });
  }


  onItemSelect(item: any): void {
    this.nextItem.roles.push(this.allRoles[item.id]);
  }

  onItemDeSelect(item: any): void {
    this.nextItem.roles = this.nextItem.roles.filter(r => r.roleType !== this.allRoles[item.id].roleType);
  }
}
