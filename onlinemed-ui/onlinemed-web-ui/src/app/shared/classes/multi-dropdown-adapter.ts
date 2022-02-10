import {IDropdownSettings} from 'ng-multiselect-dropdown';
import {Role} from '../../core/sdk/onlinemed-model';
import {RoleType} from '../../core/sdk/model-enums';

export abstract class MultiDropdownAdapter {

  getDropDownSettings(): IDropdownSettings {
    return {
      singleSelection: false,
      enableCheckAll: false,
      idField: 'id',
      textField: 'name',
    } as IDropdownSettings;
  }

  mapToDropDownObject(roles: Role[]): { id: number, name: string }[] {
    return roles.map((value, index) => {
      return {id: index, name: RoleType[value.roleType].toLowerCase()};
    });
  }
}
