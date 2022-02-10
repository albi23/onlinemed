import {TranslationStatus} from './model-enums';
import {BaseObject} from './onlinemed-model';

export interface Language extends  BaseObject{
  isMaster: boolean;
  locale: string;
readonly   localeMaster: boolean;
  staticTranslations: StaticTranslation[];
}

export interface StaticTranslation extends  BaseObject{
  language: Language;
  name: string;
  status: TranslationStatus;
  translationModule: TranslationModule;
  uiKey: string;
}

export interface TranslationModule extends  BaseObject{
  name: string;
  staticTranslations: StaticTranslation[];
}

