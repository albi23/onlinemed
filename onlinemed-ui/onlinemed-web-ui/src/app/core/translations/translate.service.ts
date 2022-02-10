import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';
import {Language, StaticTranslation} from '../sdk/model-translations';
import {LanguageCtrl, StaticTranslationCtrl} from '../sdk/onlinemed-controllers';
import {Utility} from '../../shared/utilities/utility';

@Injectable({
  providedIn: 'root'
})
export class TranslateService {

  languagesLoaded: Subject<void>;
  currentLanguage: Language;
  masterLanguage: Language;
  languages: Language[] = [];
  functionalityList: string[] = ['common'];
  translations: { [languageName: string]: { [functionalityName: string]: { [uiKey: string]: StaticTranslation } } } = {};
  private lastUpdateTime: Date = new Date();

  constructor(private languageCtrl: LanguageCtrl,
              private staticTranslationCtrl: StaticTranslationCtrl) {
    this.languagesLoaded = new Subject<void>();
  }

  getCurrentLanguage(): Language {
    return this.currentLanguage;
  }

  getMasterLanguage(): Language {
    return this.masterLanguage;
  }

  loadLanguages(): void {
    if (!this.currentLanguage) {
      this.languageCtrl.findAll()
        .subscribe((newLanguages: Language[]) => {
          this.languages = newLanguages;
          this.masterLanguage = newLanguages.find(value => (value.isMaster)) as Language;
          const browserLanguage = newLanguages.find(value => Utility.getLocaleId(value.locale) === Utility.getLocaleId(navigator.language));
          this.currentLanguage = browserLanguage ? browserLanguage : this.masterLanguage;

          for (const lang of newLanguages) {
            this.translations[lang.locale] = {};
          }
          this.languagesLoaded.next();
          this.loadTranslations('common');
          this.loadTranslations('model');
        });
    }
  }

  public loadTranslationModule(newModule: string): void {
    if (!this.translations[this.currentLanguage.locale]) {
      this.loadTranslations('common');
      this.loadTranslations('model');
    }
    if (!this.translations[this.currentLanguage.locale][newModule]) {
      this.loadTranslations(newModule);
    }
  }

  public addFunctionalities(newFunctionality: string): void {
    if (this.functionalityList.indexOf(newFunctionality) === -1) {
      this.functionalityList.push(newFunctionality);
      this.loadTranslations(newFunctionality);
    }
  }


  loadTranslations(functionality: string): void {
    if (this.currentLanguage && !this.translations[this.currentLanguage.locale][functionality]) {
      this.staticTranslationCtrl.getStaticTranslations(this.currentLanguage.locale, functionality)
        .subscribe(staticTranslations => {
          const functionalityTranslations = {};
          for (const staticTranslation of staticTranslations) {
            functionalityTranslations[staticTranslation.uiKey] = staticTranslation;
          }
          this.translations[this.currentLanguage.locale][functionality] = functionalityTranslations;
        });
    }
  }


  setLanguageForLocale(locale: string): void {
    const language = this.languages.find(value => Utility.getLocaleId(value.locale) === Utility.getLocaleId(locale));
    if (language) {
      this.setLanguage(language);
    }
  }

  setLanguage(newLanguage: Language, currentFunctionalityName?: string): void {
    this.currentLanguage = newLanguage;
    if (!this.translations[newLanguage.locale].common) {
      this.loadTranslations('common');
    }
    if (this.translations[this.currentLanguage.locale]) {
      if (!this.translations[this.currentLanguage.locale].model) {
        this.loadTranslations('model');
      }
      if (currentFunctionalityName) {
        if (!this.translations[this.currentLanguage.locale][currentFunctionalityName]) {
          this.loadTranslations(currentFunctionalityName);
        }
      }
    }
  }


  translate(key: string): string {
    const translationKeyComponents = key.split('.');
    const [functionality, ...uiKeyList] = translationKeyComponents;
    const uiKey = uiKeyList.join('.');


    if (!(this.currentLanguage && this.translations[this.currentLanguage.locale][functionality])) {
      return key;
    }

    if (this.translations[this.currentLanguage.locale][functionality][uiKey]) {
      return this.translations[this.currentLanguage.locale][functionality][uiKey].name;
    }
    // return key when not found
    return key;
  }

  getLocaleId(locale: string): string {
    return locale.substring(0, 2);
  }

}
