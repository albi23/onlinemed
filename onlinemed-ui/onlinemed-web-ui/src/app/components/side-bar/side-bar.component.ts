import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../core/auth/auth.service';
import {Functionality} from '../../core/sdk/onlinemed-model';
import {Router} from '@angular/router';
import {TranslateService} from '../../core/translations/translate.service';
import {SideBarIconMapper} from './side-bar-icon-mapper';
import {Language} from '../../core/sdk/model-translations';
import {SideBarService} from '../../shared/services/side-bar.service';
import {NotificationWrapComponent} from '../../shared/components/notification-box/notification-wrap.component';
import {Alert, AlertType} from '../../shared/classes/alert';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styles: []
})
export class SideBarComponent implements OnInit {
  private personFunctionalities: Functionality[] = [];
  private currentActiveFunctionalityIndex = -1;
  languages: Language[] = [];
  selectedMenuIndex: number;
  status = false;

  constructor(private authService: AuthService,
              private router: Router,
              private translateService: TranslateService,
              private sideBarService: SideBarService) {
  }


  ngOnInit(): void {
    this.personFunctionalities = this.authService.getCurrentLoggedPersonFunctionalities();
    this.languages = [...this.translateService.languages];
    this.selectedMenuIndex = this.languages.findIndex(lang => lang.locale === this.translateService.getCurrentLanguage().locale);
    this.showNotificationAlert();
  }


  getPersonFunctionalities(): Functionality[] {
    return this.personFunctionalities;
  }

  onClickRedirect(functionality: Functionality, index: number): void {
    this.currentActiveFunctionalityIndex = index;
    this.router.navigate(['/om/' + functionality.name]);
  }

  public isActiveFunctionality(index: number): boolean {
    return this.currentActiveFunctionalityIndex === index;
  }

  public translateName(functionalityName: string): string {
    return this.translateService.translate('common.' + functionalityName);
  }

  getFunctionalityIcon(funcName: string): string {
    return SideBarIconMapper.getCssIcon(funcName);
  }

  getLanguageIcon(languageName: string): string {
    return SideBarIconMapper.getFlagIcon(languageName);
  }

  getDateYear(): number {
    return new Date().getFullYear();
  }

  onChangeLanguage(): void {
    this.translateService.setLanguage(this.languages[this.selectedMenuIndex], this.extractCurrentFunctionality());
  }

  onClickMenuIcon(): void {
    this.status = !this.status;
    this.sideBarService.updateState(!this.status);
  }

  private extractCurrentFunctionality(): string {
    const currentURL = this.router.routerState.snapshot.url;
    const functionality = currentURL.split('/om/')[1].split(/\?|\//, 1)[0];
    return functionality;
  }

  onLogOut(): void {
    this.authService.logout();
  }

  private showNotificationAlert(): void {
    const notifications = this.authService.getCurrentLoggedPerson().notifications || [];
    if (!notifications.isEmpty()) {
      NotificationWrapComponent.sendAlert(new Alert(AlertType.INFO, 'common.check-new-notifications'));
    }
  }
}
