import {BaseEntity} from './core-model';
import {NotificationType, RoleType, VisitState} from './model-enums';

export interface BaseObject extends  BaseEntity{
  timestamp: number;
  version: number;
}

export interface CalendarEvent extends  BaseObject{
  endDate: number;
  person: Person;
  title: string;
}

export interface Community extends  BaseObject{
  comments: number;
  description: string;
  lastLogin: number;
  user: Person;
}

export interface DoctorInfo extends  BaseObject{
  doctorType: string;
  facilityLocations: FacilityLocation[];
  person: Person;
  phoneNumber: string;
  specialization: string;
}

export interface FacilityLocation extends  BaseObject{
  doctorInfo: DoctorInfo;
  facilityAddress: string;
  facilityName: string;
  visitsPriceList: {[key: string] :string};
}

export interface ForumCategory extends  BaseObject{
  description: string;
  icon: string;
  translationName: string;
}

export interface ForumPost extends  BaseObject{
  forumTopic: ForumTopic;
  postCreator: Person;
  text: string;
}

export interface ForumTopic extends  BaseObject{
  creatorUserName: string;
  favorites: string[];
  forumCategory: ForumCategory;
  hasztags: string;
  title: string;
}

export interface Functionality extends  BaseObject{
  name: string;
}

export interface Notification extends  BaseObject{
  name: string;
  notificationType: NotificationType;
  person: Person;
  senderId: string;
  surname: string;
  visit: Visit | null;
}

export interface Person extends  BaseObject{
  calendarEvents: CalendarEvent[];
  community: Community;
  defaultLanguage: string;
  doctorInfo: DoctorInfo;
  email: string;
  name: string;
  notifications: Notification[];
  phoneNumber: string;
  roles: Role[];
  security: Security;
  surname: string;
  userName: string;
}

export interface RegistrationLink extends  BaseObject{
  roleType: RoleType[];
}

export interface Role extends  BaseObject{
  functionalities: Functionality[];
  roleType: RoleType;
}

export interface Security extends  BaseObject{
  hash: string;
  person: Person;
  securityToken: string;
  token: string;
}

export interface Visit extends  BaseObject{
  localisation: string;
  optionalMessage: string;
  visitDate: number;
  visitState: VisitState;
  visitType: string;
}

