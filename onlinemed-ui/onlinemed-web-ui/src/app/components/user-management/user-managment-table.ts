import {Component, ComponentFactoryResolver, EventEmitter, Injector, OnInit, Output, Type} from '@angular/core';
import {Person, Role} from '../../core/sdk/onlinemed-model';
import {BigTableComponent} from '../../shared/components/base-table/big-table';
import {TranslateService} from '../../core/translations/translate.service';
import {RoleType} from '../../core/sdk/model-enums';
import {PersonCtrl, RoleCtrl} from '../../core/sdk/onlinemed-controllers';
import {EditRoles, OBJECT_TOKEN, UserManagementEditComponent} from './user-management-edit/user-management-edit.component';
import {Utility} from '../../shared/utilities/utility';
import {NotificationWrapComponent} from '../../shared/components/notification-box/notification-wrap.component';
import {Alert, AlertType} from '../../shared/classes/alert';
import {ModalService} from '../../shared/services/modal.service';

@Component({
  selector: 'app-user-management-table',
  templateUrl: '../../shared/components/base-table/base-table.component.html',
  styles: []
})
export class UserManagementTableComponent extends BigTableComponent<Person> implements OnInit {

  columns: any[] = ['name', 'surname', 'email', 'userName', 'roles', 'action'];
  unSortableColumns = ['roles'];
  currentComponent: Type<any> = UserManagementEditComponent;
  specificColumnName = 'roles';
  @Output() toggleSpinner: EventEmitter<boolean> = new EventEmitter<boolean>();
  private allRoles: Role[] = [];
  private personClone: Person | null;

  constructor(protected translateService: TranslateService,
              protected resolver: ComponentFactoryResolver,
              private personCtrl: PersonCtrl,
              private roleCtrl: RoleCtrl,
              private inj: Injector,
              private modalService: ModalService) {
    super(translateService, resolver);
  }

  ngOnInit(): void {
    this.translateService.loadTranslationModule('user-management');
    this.loadAllRoles();
  }

  loadAllRoles(): void {
    this.roleCtrl.findAll().subscribe((res: Role[]) => this.allRoles = res,
      (err) => Utility.showViolationsIfOccurs(err));
  }

  executeQuery(): void {
    this.loadPeople();
  }

  cancelEdition(): void {
    this.editDisabled = true;
    this.updatePageSource(this.personClone as Person);
    this.clearEdited();
  }

  deleteItemAction(item: Person): void {
    this.modalService.injectDataToModal('user-management.remove-person-dialog-body', 'common.warning');
    this.modalService.onConfirm(_ => {
      this.removeFromSelected(this.pageItems.find(p => p.id === item.id) as Person);
      this.personCtrl.deleteObject(item.id.split('/')[1]).subscribe(_ => super.refresh());
      this.modalService.closeModal();
    });
    this.modalService.onCancel(_ => this.modalService.closeModal());
    this.modalService.openModal();
  }

  editItem(editedItem: Person): void {
    this.editDisabled = false;
    this.editedItem = editedItem;
    this.createInjector(editedItem);
    this.personClone = (Utility.clone(editedItem, true) as Person);

  }

  createInjector(nextItem: Person): void {
    this.currentInjector = Injector.create({
      providers: [
        {provide: EditRoles, deps: [OBJECT_TOKEN]},
        {provide: OBJECT_TOKEN, useValue: {nextItem, allRoles: this.allRoles}},
      ],
      parent: this.inj,
    });
  }

  protected executeCountQuery(): void {
    this.personCtrl.countAll().subscribe((itemsNumber: number) => this.itemsNumber = itemsNumber);
  }

  getColumnNumber(): number {
    return this.columns.length;
  }

  getColumnTranslation(column: string): string {
    return this.translateService.translate('user-management.' + column);
  }

  getColumns(): string[] {
    return this.columns;
  }

  getValueFromItem(item: Person, column: string): string | any {
    switch (column) {
      case 'name':
        return item.name;
      case 'surname':
        return item.surname;
      case 'email':
        return item.email;
      case 'userName':
        return item.userName;
      case 'roles': {
        return item.roles.map(r => RoleType[r.roleType].toLowerCase()).join(', ');
      }
    }
  }

  onChange(): void {
  }

  defineExtraCondition(nextItem: Person): boolean {
    return (this.editedItem || false) && (nextItem.id === this.editedItem.id);
  }

  updateItem(item: Person): void {
    this.editDisabled = true;
    if (this.validateBeforeUpdate()) {
      this.toggleSpinner.emit(true);
      this.personCtrl.updateObject(this.editedItem as Person).subscribe(updatedPerson => {
        this.updatePageSource(updatedPerson);
        this.loadAllRoles();
        this.clearEdited();
        this.toggleSpinner.emit(false);
        Utility.updateSuccessfullyNotification();
      }, error => {
        this.toggleSpinner.emit(false);
        this.clearEdited();
        if (!Utility.showViolationsIfOccurs(error)) {
          Utility.updateRejectedNotification();
        }
      });
    } else {
      this.updatePageSource(this.personClone as Person);
      this.clearEdited();
    }
  }

  private updatePageSource(newPersonState: Person): void {
    const index = this.pageItems.findIndex(person => person.id === newPersonState.id);
    this.pageItems[index] = newPersonState;
  }

  private clearEdited(): void{
    this.personClone = null;
    this.editedItem = null;
  }

  updateValues(item: Person, column: string, newValue: any): any {
    switch (column) {
      case 'name':
        return item.name = newValue;
      case 'surname':
        return item.surname = newValue;
      case 'email':
        return item.email = newValue;
      case 'userName':
        return item.userName = newValue;
      case 'roles': {
        return item.roles = newValue;
      }
    }
  }


  private loadPeople(): void {
    this.personCtrl.getPeople(this.sortByColumn, this.ascending, this.pageNumber, this.pageSize)
      .subscribe((people: Person[]) => this.pageItems = people);
  }

  private validateBeforeUpdate(): boolean {
    const oldObjHasAdminRole = (this.personClone as Person).roles.findIndex(r => r.roleType === RoleType.ADMINISTRATOR) > -1;
    const editedObjWithoutAdminRole = (this.editedItem?.roles || []).findIndex(r => r.roleType === RoleType.ADMINISTRATOR) < 0;
    if (oldObjHasAdminRole && editedObjWithoutAdminRole) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.DANGER, 'user-management.cannot-remove-admin-role'));
      return false;
    }
    if ((this.personClone as Person).roles.length !== this.editedItem?.roles.length) {
      return true;
    }
    for (const role of this.editedItem.roles) {
      if ((this.personClone as Person).roles.findIndex(r => r.roleType === role.roleType) < 0) {
        return true;
      }
    }
    if (oldObjHasAdminRole && editedObjWithoutAdminRole) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.DANGER, 'user-management.cannot-remove-admin-role'));
      return false;
    }

    if (this.containsEmptyFieldsAfterUpdate()){
      NotificationWrapComponent.sendAlert(new Alert(AlertType.DANGER, 'Entered empty fields!'));
      return false;
    }
    return JSON.stringify(this.editedItem) !== JSON.stringify(this.personClone);
  }

  private containsEmptyFieldsAfterUpdate(): boolean {
   return ![this.editedItem?.userName, this.editedItem?.surname, this.editedItem?.name, this.editedItem?.email]
      .every((value: string | undefined) => value && !(value as string).isEmpty());
  }

}
