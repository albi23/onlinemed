import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {RegistrationLink, Role} from '../../../core/sdk/onlinemed-model';
import {MultiDropdownAdapter} from '../../../shared/classes/multi-dropdown-adapter';
import {Utility} from '../../../shared/utilities/utility';
import {RegistrationLinkCtrl, RoleCtrl} from '../../../core/sdk/onlinemed-controllers';
import {ModalService} from '../../../shared/services/modal.service';
import {NotificationWrapComponent} from '../../../shared/components/notification-box/notification-wrap.component';
import {Alert, AlertType} from '../../../shared/classes/alert';
import {RoleType} from '../../../core/sdk/model-enums';
import {TranslateService} from '../../../core/translations/translate.service';
import {environment} from '../../../../environments/environment';

export class ExpirationTime {
  label: string;
  value: number;
}

@Component({
  selector: 'app-link-generation',
  templateUrl: './link-generation.component.html',
  styles: []
})
export class LinkGenerationComponent extends MultiDropdownAdapter implements OnInit {

  @ViewChild('amount') amountOfLinks: ElementRef;
  allRoles: Role[] = [];
  selectedItems: { id: number, name: string }[] = [];
  dropdownList: { id: number, name: string }[] = [];
  expirationTime: ExpirationTime[] = [];
  selectedExpirationTimeIndex = 0;
  private BACKEND_URL: string;

  private selectedRoles: Role[] = [];

  constructor(private roleCtrl: RoleCtrl,
              private modalService: ModalService,
              private translateService: TranslateService,
              private registrationLinkCtrl: RegistrationLinkCtrl) {
    super();
    this.fillExpirationTimeArray();
  }

  ngOnInit(): void {
    this.loadAllRoles();
    this.setServerIp();
  }


  private loadAllRoles(): void {
    this.roleCtrl.findAll().subscribe((res: Role[]) => {
        this.allRoles = res.filter(r => r.roleType !== RoleType.ADMINISTRATOR) || [];
        this.dropdownList = this.mapToDropDownObject(this.allRoles);
      },
      (err) => Utility.showViolationsIfOccurs(err));
  }

  private setServerIp(): void {
    if (!environment.production) {
      this.registrationLinkCtrl.getServerIp().subscribe((ip: string) => {
        if (!ip) {
          this.BACKEND_URL = 'http://localhost:4201';
        } else {
          this.BACKEND_URL = 'http://' + ip + ':4201';
        }
      });
    }else {
      this.BACKEND_URL = environment.BASE_UI_URL;
    }
  }

  onItemSelect(item: any): void {
    this.selectedRoles.push((this.allRoles as Role[])[item.id]);
  }

  onItemDeSelect(item: any): void {
    this.selectedRoles = this.selectedRoles.filter(r => r.roleType !== (this.allRoles as Role[])[item.id].roleType);
  }

  onClickGenerate(): void {

    const amount = +this.amountOfLinks.nativeElement.value || -1;
    if (amount < 1 || amount > 20) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'Correct links range is [1, 20]'));
      return;
    }
    if (this.selectedRoles.isEmpty()) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'Please select target user roles'));
      return;
    }

    this.saveLinksInDb(this.createNewRegistrationLinks(amount));
  }

  private saveLinksInDb(links: RegistrationLink[]): void {
    this.registrationLinkCtrl.createRegistrationLinks(links).subscribe((res: RegistrationLink[]) => {
      const urlsString = res.map(r => this.BACKEND_URL.concat('/register/').concat(Utility.getIdFromObject(r))).join('\n');
      this.showResultModal(urlsString);
    }, error => Utility.showViolationsIfOccurs(error));
  }

  private showResultModal(urlsString: string): void {
    this.modalService.injectDataToModal2(urlsString, 'Result Links');
    this.modalService.onConfirm(_ => {
      this.modalService.closeModal();
    });
    this.modalService.onCancel(_ => this.modalService.closeModal());
    this.modalService.openModal();
  }

  private fillExpirationTimeArray(): void {
    const dataArray: number[] = [1, 2, 3, 6, 12, 24, 48, 72];
    this.expirationTime = dataArray.map((value) => {
      if (value % 24 !== 0) {
        return {label: String(value).concat(' h'), value: value * 3_600_000} as ExpirationTime;
      } else {
        return {label: String(value / 24).concat(' day(s)'), value: value * 3_600_000} as ExpirationTime;
      }
    });
  }

  private createNewRegistrationLinks(amount: number): RegistrationLink[] {

    const links: RegistrationLink[] = [];
    const roleTypes: RoleType[] = this.selectedRoles.map(r => r.roleType);
    if (this.selectedRoles.findIndex(r => r.roleType === RoleType.USER) < 0){
      roleTypes.push(RoleType.USER);
    }
    for (let i = 1; i <= amount; i++) {
      const defaultBaseObject = Utility.getDefaultBaseObject('registration-link');
      defaultBaseObject.timestamp += this.expirationTime[this.selectedExpirationTimeIndex].value;
      links.push({roleType: roleTypes, ...defaultBaseObject});
    }
    return links;
  }

  getPlaceholder(): string {
   return  this.translateService.translate('user-management.registration-link-generator');
  }
}
