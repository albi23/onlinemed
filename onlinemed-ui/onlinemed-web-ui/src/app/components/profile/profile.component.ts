import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {AuthService} from '../../core/auth/auth.service';
import {Community, Person} from '../../core/sdk/onlinemed-model';
import {RoleType} from '../../core/sdk/model-enums';
import {NotificationWrapComponent} from '../../shared/components/notification-box/notification-wrap.component';
import {Alert, AlertType} from '../../shared/classes/alert';
import {TranslateService} from '../../core/translations/translate.service';
import {CommunityCtrl, PersonCtrl} from '../../core/sdk/onlinemed-controllers';
import {Utility} from '../../shared/utilities/utility';
import {ActivatedRoute, NavigationStart, Router} from '@angular/router';
import {filter} from 'rxjs/operators';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styles: []
})
export class ProfileComponent implements OnInit {
  @ViewChild('contentHolder') profileContent: ElementRef;
  @ViewChild('textArea') textAreaContent: ElementRef;

  currPerson: Person;
  communityInfo: Community = {comments: 0, description: '', lastLogin: new Date().getTime(), user: this.currPerson} as Community;
  editProfile = false;
  profileIsEditable = true;
  isCustomUser = true;
  paramID = '';

  constructor(private authService: AuthService,
              private translationService: TranslateService,
              private route: ActivatedRoute,
              private communityCtrl: CommunityCtrl,
              private personCtrl: PersonCtrl,
              private router: Router) {
    this.route.queryParams.subscribe(params => this.paramID = params.id || '');
    this.router.events.pipe(filter(event => event instanceof NavigationStart)).subscribe((res: NavigationStart) => {
      if (!this.paramID.isEmpty() && !res.url.includes('?id')) {
        this.paramID = '';
        this.ngOnInit();
      }
    });
    this.isCustomUser = this.authService.getCurrentLoggedPerson().roles.map(r => r.roleType)
      .reduce((a, b) => a  + b , 0) === 0;
  }

  ngOnInit(): void {
    this.translationService.loadTranslationModule('profile');
    this.profileIsEditable = this.paramID.isEmpty();
    const searchId = (this.profileIsEditable) ? this.authService.getCurrentLoggedPerson().id : this.paramID;
    if (!this.profileIsEditable) {
      this.personCtrl.getObject(Utility.getObjectId(searchId)).subscribe((res: Person) => this.currPerson = res);
    } else {
      this.currPerson = this.authService.getCurrentLoggedPerson();
    }
    this.communityCtrl.getObject(Utility.getObjectId(searchId)).subscribe((res: Community) => {
      this.communityInfo = res;
    }, error => {
      Utility.showViolationsIfOccurs(error);
      this.communityInfo = {comments: 0, description: '', lastLogin: new Date().getTime(), user: this.currPerson} as Community;
    });

  }


  getPersonRole(): string {
    const roleType = this.currPerson.roles.sort((a, b) => b.roleType - a.roleType)[0].roleType;
    const roleName: string = RoleType[roleType];
    return roleName.substr(0, 1).concat(roleName.substr(1).toLowerCase());
  }

  clickEditTextProfile(): void {
    if (this.editProfile) {
      this.textAreaContent.nativeElement.value = '...';
      this.editProfile = false;
    } else {
      const value = this.profileContent.nativeElement.textContent;
      this.editProfile = true;
      this.textAreaContent.nativeElement.value = value;
    }

  }


  onClickApply(): void {
    const textAreaContent = this.textAreaContent.nativeElement.value;
    if (textAreaContent.split('\n').length > 10) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.WARNING, 'profile.max-content-line-is-10'));
      return;
    }
    this.communityInfo.description = textAreaContent;
    this.communityCtrl.updateObject(this.communityInfo).subscribe((res: Community) => {
      this.communityInfo = res;
      this.profileContent.nativeElement.textContent = res.description;
      this.editProfile = false;
      Utility.updateSuccessfullyNotification();

    }, error => {
      Utility.showViolationsIfOccurs(error);
      Utility.updateRejectedNotification();
    });
  }
}
