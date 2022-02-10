import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {PersonCtrl, RegistrationLinkCtrl} from '../sdk/onlinemed-controllers';
import {Community, DoctorInfo, FacilityLocation, Person, RegistrationLink} from '../sdk/onlinemed-model';
import {NotificationWrapComponent} from '../../shared/components/notification-box/notification-wrap.component';
import {Alert, AlertType} from '../../shared/classes/alert';
import {RoleType} from '../sdk/model-enums';
import {Utility} from '../../shared/utilities/utility';
import {Observable, Subject} from 'rxjs';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {JsonScopedSerializer} from '../sdk/JsonParser';
import {JsonScope} from '../sdk/jsonScope';
import {map} from 'rxjs/operators';
import {ErrorHandlerService} from '../sdk/error-handler.service';
import {environment} from '../../../environments/environment.prod';

export interface DoctorServices {
  id: string;
  name: string;
  price: string;
}

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styles: []
})
export class RegistrationComponent implements OnInit {

  spinnerStatus = false;
  nameField: string;
  phoneField: string;
  emailField: string;
  surnameField: string;
  passwordField: string;
  usernameField: string;
  doctorTypeField: string;
  specialisationField: string;
  facilityPlaceField: string;
  facilityNameField: string;
  isDoctorRegistration = true;
  currentStep = 1;
  doctorServices: DoctorServices[] = [];
  private currentLinkId: string;
  private loginPersonObject: Person;
  private registrationRoleTypes: RoleType[] = [];

  constructor(private router: Router,
              private route: ActivatedRoute,
              private registrationLinkCtrl: RegistrationLinkCtrl,
              private personCtrl: PersonCtrl,
              private httpService: HttpClient,
              private errorHandlerService: ErrorHandlerService) {
    this.route.params.subscribe(param => this.currentLinkId = param.id || '88df1934-0a91-4bd3-b14e-18888e20aed5');
    this.validateParameter();
  }

  ngOnInit(): void {
  }

  private toggleSpinnerStatus(): void {
    this.spinnerStatus = !this.spinnerStatus;
  }

  private findLinkInDb(): void {
    this.toggleSpinnerStatus();
    this.registrationLinkCtrl.getObject(this.currentLinkId).subscribe((res: RegistrationLink) => {
        this.toggleSpinnerStatus();
        if (Utility.getIdFromObject(res) !== this.currentLinkId || (res.timestamp - new Date().getTime()) <= 0) {
          NotificationWrapComponent.sendAlert(new Alert(AlertType.DANGER, 'Registration link has expired'));
          this.onClickSignIn();
        }
        this.registrationRoleTypes = res.roleType;
        this.isDoctorRegistration = res.roleType.filter(r => r === RoleType.DOCTOR).length > 0;
      }, error => {
        this.toggleSpinnerStatus();
        Utility.showViolationsIfOccurs(error);
      }
    );
  }

  private validateParameter(): void {
    const testUUIDRegex = /^[0-9A-F]{8}-[0-9A-F]{4}-[4][0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}$/i;
    if (!this.currentLinkId || !testUUIDRegex.test(this.currentLinkId)) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.DANGER, 'Incorrect registration link'));
      this.onClickSignIn();
    } else {
      this.findLinkInDb();
    }
  }

  onClickSignIn(): void {
    this.router.navigate(['/login']);
  }

  increaseStep(): void {
    this.currentStep++;
  }

  onRemoveDoctorService(i: number): void {
    this.doctorServices.splice(i, 1);
  }

  onAddService(): void {
    this.doctorServices.push({id: Utility.getUUID(), name: '', price: ''});
  }

  addNewMedicalLocalisation(): boolean {
    const fields = [this.facilityNameField, this.facilityPlaceField];
    if (this.areAllFieldFilled(fields) && this.checkDefinedServices()) {
      const localisation = this.getDefaultLocalisation(this.loginPersonObject.doctorInfo.id);
      localisation.facilityAddress = this.facilityPlaceField;
      localisation.facilityName = this.facilityNameField;
      for (const service of this.doctorServices) {
        localisation.visitsPriceList[service.name] = service.price;
      }
      this.facilityNameField = '';
      this.facilityPlaceField = '';
      this.doctorServices = [];
      this.loginPersonObject.doctorInfo.facilityLocations.push(localisation);
      return true;
    }
    return false;
  }

  validateNextStep(): void {
    switch (this.currentStep) {
      case 1: {
        if (this.checkFirstStep()) {
          this.personCtrl.isUsernameUsed(this.usernameField).subscribe((res: boolean) => {
            if (res) {
              NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'Username is already in use'));
            } else {
              this.increaseStep();
              this.createPersonObject();
            }
          });
        }
        break;
      }
      case 2: {
        this.validateStepTwo();
        break;
      }
    }
  }

  private checkFirstStep(): boolean {
    const fieldsValues = [this.passwordField, this.usernameField,
      this.nameField, this.surnameField, this.emailField];

    if (!this.areAllFieldFilled(fieldsValues)) {
      return false;
    }
    if (this.passwordField.length < 6) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'To short password'));
      return false;
    }

    if (this.passwordField.length < 6) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'to short password'));
      return false;
    }

    return true;
  }

  private areAllFieldFilled(fieldsValues: string[]): boolean {
    for (const val of fieldsValues) {
      if (!val || val.isEmpty()) {
        NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'Fill all required fields'));
        return false;
      }
    }
    return true;
  }

  private createPersonObject(): void {

    const personObj = Utility.getDefaultBaseObject('person');

    this.loginPersonObject = {
      calendarEvents: [],
      community: this.getDefaultCommunityObj(personObj.id),
      defaultLanguage: 'en_GB',
      // @ts-ignore
      doctorInfo: (this.isDoctorRegistration) ? this.getDefaultDoctorInfo(personObj.id) : null,
      email: this.emailField,
      name: this.nameField,
      notifications: [],
      phoneNumber: '',
      roles: [],
      // @ts-ignore
      security: null,
      surname: this.emailField,
      userName: this.emailField,
      ...personObj
    };
  }

  private getMappedRoles(): string {
    return this.registrationRoleTypes.map(r => String(r)).join(';');
  }

  private getDefaultCommunityObj(personId: string): Community {
    const defaultBaseObject = Utility.getDefaultBaseObject('community');
    defaultBaseObject.id = 'community/' + Utility.getObjectId(personId);
    return {
      comments: 0,
      description: '',
      lastLogin: new Date().getTime(),
      user: {id: personId} as Person,
      ...defaultBaseObject,
    } as Community;
  }

  private getDefaultDoctorInfo(personId: string): DoctorInfo {
    return {
      doctorType: '',
      facilityLocations: [],
      person: {id: personId} as Person,
      phoneNumber: '',
      specialization: '',
      ...Utility.getDefaultBaseObject('doctor-info')
    } as DoctorInfo;
  }

  private validateStepTwo(): void {
    const values = [this.specialisationField, this.doctorTypeField, this.phoneField];
    if (this.areAllFieldFilled(values)) {
      this.loginPersonObject.doctorInfo.doctorType = this.doctorTypeField;
      this.loginPersonObject.doctorInfo.specialization = this.specialisationField;
      this.loginPersonObject.doctorInfo.phoneNumber = this.phoneField;
      this.increaseStep();
    }
  }

  private checkDefinedServices(): boolean {
    if (this.doctorServices.isEmpty()) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'No services defined'));
      return false;
    }
    for (const service of this.doctorServices) {
      if (!this.areAllFieldFilled([service.price, service.name])) {
        return false;
      }
    }
    return true;
  }

  private getDefaultLocalisation(doctorInfoId: string): FacilityLocation {
    return {
      doctorInfo: {id: doctorInfoId} as DoctorInfo,
      facilityAddress: '',
      facilityName: '',
      visitsPriceList: {},
      ...Utility.getDefaultBaseObject('facility-location')
    } as FacilityLocation;
  }

  onClickRegister(): void {
    if (this.isDoctorRegistration) {
      if (!this.loginPersonObject.doctorInfo.facilityLocations.isEmpty() || this.addNewMedicalLocalisation()) {
        this.saveObj();
      }
    } else {
      this.createPersonObject();
      this.saveObj();
    }
  }

  private saveObj(): void {

    this.createObject(this.loginPersonObject, this.currentLinkId, this.passwordField, this.getMappedRoles()).subscribe(
      (res: Person) => {
        NotificationWrapComponent.sendAlert(new Alert(AlertType.SUCCESS, 'Succes Registration'));
        this.onClickSignIn();
      }, error => {
        Utility.showViolationsIfOccurs(error);
      }
    );
  }

  private createObject(entity: Person, id: string, passwordPlain: string, roles: string): Observable<Person> {
    const params = new HttpParams().append('rolesType', roles);
    const headers = new HttpHeaders().set('Content-type', 'application/json')
      .append('Authorization', 'Basic ' + btoa(passwordPlain));
    const subject = new Subject<Person>();
    this.httpService.post(environment.BASE_UR + '/api/person/' + id + '/register',
      JsonScopedSerializer.stringify(entity, new JsonScope(false, [])), {headers, params, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}
