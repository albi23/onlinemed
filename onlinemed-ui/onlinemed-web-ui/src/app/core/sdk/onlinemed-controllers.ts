import {JsonScopedSerializer} from './JsonParser';
import {ErrorHandlerService} from './error-handler.service';
import {JsonScope} from './jsonScope';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {BaseEntity} from './core-model';
import {DrugHints, DrugInfo, Mail} from './model-dto';
import {Language, StaticTranslation} from './model-translations';
import {
  BaseObject,
  CalendarEvent,
  Community,
  DoctorInfo,
  ForumCategory,
  ForumPost,
  ForumTopic,
  Notification,
  Person,
  RegistrationLink,
  Role
} from './onlinemed-model';
import {Observable, Subject} from 'rxjs';
import {map, take} from 'rxjs/operators';
import {environment} from 'src/environments/environment';

/* tslint:disable  max-line-length  align  eofline   */

@Injectable()
export class CalendarEventCtrl {
  errorHandlerService: ErrorHandlerService;
  httpService: HttpClient;


  public constructor(httpService: HttpClient, errorHandlerService: ErrorHandlerService) {
    this.httpService = httpService;
    this.errorHandlerService = errorHandlerService;
  }

  public deleteObject(id: string): Observable<void>  {
    const subject = new Subject<void>();
    this.httpService.delete<void>(environment.BASE_URL + '/api/calendar-event/' + id + '')
      .pipe(take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getUserEvents(arg0: string): Observable<CalendarEvent[]>  {
    const subject = new Subject<CalendarEvent[]>();
    this.httpService.get(environment.BASE_URL + '/api/calendar-event/user/' + arg0 + '', {responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public removeUserEvents(arg0: string[]): Observable<void>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<void>();
    this.httpService.post<void>(environment.BASE_URL + '/api/calendar-event/remove-many', arg0 , {headers})
      .pipe(take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateObject(entity: BaseEntity): Observable<CalendarEvent>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<CalendarEvent>();
    this.httpService.put(environment.BASE_URL + '/api/calendar-event/' + entity.id.split('/')[1] + '', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])) , {headers, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateUserEvents(arg0: CalendarEvent[]): Observable<CalendarEvent[]>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<CalendarEvent[]>();
    this.httpService.post(environment.BASE_URL + '/api/calendar-event/update-many', JsonScopedSerializer.stringify(arg0, new JsonScope(false, [])) , {headers, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

@Injectable()
export class CommunityCtrl {
  errorHandlerService: ErrorHandlerService;
  httpService: HttpClient;


  public constructor(httpService: HttpClient, errorHandlerService: ErrorHandlerService) {
    this.httpService = httpService;
    this.errorHandlerService = errorHandlerService;
  }

  public getObject(id: string): Observable<Community>  {
    const subject = new Subject<Community>();
    this.httpService.get(environment.BASE_URL + '/api/community/' + id + '', {responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateObject(entity: BaseEntity): Observable<Community>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<Community>();
    this.httpService.put(environment.BASE_URL + '/api/community/' + entity.id.split('/')[1] + '', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])) , {headers, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

@Injectable()
export class DoctorInfoCtrl {
  errorHandlerService: ErrorHandlerService;
  httpService: HttpClient;


  public constructor(httpService: HttpClient, errorHandlerService: ErrorHandlerService) {
    this.httpService = httpService;
    this.errorHandlerService = errorHandlerService;
  }

  public findAll(): Observable<DoctorInfo[]>  {
    const subject = new Subject<DoctorInfo[]>();
    this.httpService.get(environment.BASE_URL + '/api/doctor-info', {responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateObject(arg0: BaseEntity): Observable<DoctorInfo>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<DoctorInfo>();
    this.httpService.put(environment.BASE_URL + '/api/doctor-info/' + arg0.id.split('/')[1] + '', JsonScopedSerializer.stringify(arg0, new JsonScope(false, [])) , {headers, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

@Injectable()
export class DrugEquivalentsCtrl {
  errorHandlerService: ErrorHandlerService;
  httpService: HttpClient;


  public constructor(httpService: HttpClient, errorHandlerService: ErrorHandlerService) {
    this.httpService = httpService;
    this.errorHandlerService = errorHandlerService;
  }

  public searchDrugHints(arg0: string): Observable<DrugHints[]>  {
    const queryParamsList: { name: string, value: string }[] = [];
    queryParamsList.push({name: 'search', value: arg0});
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const subject = new Subject<DrugHints[]>();
    this.httpService.get(environment.BASE_URL + '/api/drug-equivalent/hints', {params, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public searchDrugInfo(arg0: string): Observable<DrugInfo[]>  {
    const queryParamsList: { name: string, value: string }[] = [];
    queryParamsList.push({name: 'url', value: arg0});
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const subject = new Subject<DrugInfo[]>();
    this.httpService.get(environment.BASE_URL + '/api/drug-equivalent/drug-info', {params, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

@Injectable()
export class EmailSendCtrl {
  errorHandlerService: ErrorHandlerService;
  httpService: HttpClient;


  public constructor(httpService: HttpClient, errorHandlerService: ErrorHandlerService) {
    this.httpService = httpService;
    this.errorHandlerService = errorHandlerService;
  }

  public sendMessageMail(arg0: Mail, arg1: string, arg2: string, arg3: string): Observable<boolean>  {
    const queryParamsList: { name: string, value: string }[] = [];
    queryParamsList.push({name: 'sender', value: arg2});

    queryParamsList.push({name: 'receiver', value: arg3});
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<boolean>();
    this.httpService.post<boolean>(environment.BASE_URL + '/api/email/' + arg1 + '/send', arg0 , {headers, params})
      .pipe(take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

@Injectable()
export class ForumCategoryCtrl {
  errorHandlerService: ErrorHandlerService;
  httpService: HttpClient;


  public constructor(httpService: HttpClient, errorHandlerService: ErrorHandlerService) {
    this.httpService = httpService;
    this.errorHandlerService = errorHandlerService;
  }

  public findAll(): Observable<ForumCategory[]>  {
    const subject = new Subject<ForumCategory[]>();
    this.httpService.get(environment.BASE_URL + '/api/forum-category', {responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

@Injectable()
export class ForumPostCtrl {
  errorHandlerService: ErrorHandlerService;
  httpService: HttpClient;


  public constructor(httpService: HttpClient, errorHandlerService: ErrorHandlerService) {
    this.httpService = httpService;
    this.errorHandlerService = errorHandlerService;
  }

  public createObject(entity: BaseEntity): Observable<ForumPost>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<ForumPost>();
    this.httpService.post(environment.BASE_URL + '/api/forum-post', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])) , {headers, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public deleteObject(id: string): Observable<void>  {
    const subject = new Subject<void>();
    this.httpService.delete<void>(environment.BASE_URL + '/api/forum-post/' + id + '')
      .pipe(take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getPaginatedTopicPosts(arg0: string, arg1: number | null = 0, arg2: string = "timestamp", arg3: boolean | null = true, arg4: number | null = 10): Observable<ForumPost[]>  {
    const queryParamsList: { name: string, value: string }[] = [];
    if (arg1 !== undefined && arg1 !== null) {
      queryParamsList.push({name: 'pageNumber', value: arg1.toString()});
    }

    if (arg2 !== undefined && arg2 !== null) {
      queryParamsList.push({name: 'sortBy', value: arg2});
    }

    if (arg3 !== undefined && arg3 !== null) {
      queryParamsList.push({name: 'ascending', value: arg3.toString()});
    }

    if (arg4 !== undefined && arg4 !== null) {
      queryParamsList.push({name: 'pageSize', value: arg4.toString()});
    }
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const subject = new Subject<ForumPost[]>();
    this.httpService.get(environment.BASE_URL + '/api/forum-post/' + arg0 + '/topic-posts', {params, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getTopicPostCount(arg0: string, arg1: number | null = 0, arg2: string = "timestamp", arg3: boolean | null = true, arg4: number | null = 10): Observable<number>  {
    const queryParamsList: { name: string, value: string }[] = [];
    if (arg1 !== undefined && arg1 !== null) {
      queryParamsList.push({name: 'pageNumber', value: arg1.toString()});
    }

    if (arg2 !== undefined && arg2 !== null) {
      queryParamsList.push({name: 'sortBy', value: arg2});
    }

    if (arg3 !== undefined && arg3 !== null) {
      queryParamsList.push({name: 'ascending', value: arg3.toString()});
    }

    if (arg4 !== undefined && arg4 !== null) {
      queryParamsList.push({name: 'pageSize', value: arg4.toString()});
    }
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const subject = new Subject<number>();
    this.httpService.get<number>(environment.BASE_URL + '/api/forum-post/' + arg0 + '/topic-posts/count', {params})
      .pipe(take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateObject(arg0: BaseEntity): Observable<ForumPost>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<ForumPost>();
    this.httpService.put(environment.BASE_URL + '/api/forum-post/' + arg0.id.split('/')[1] + '', JsonScopedSerializer.stringify(arg0, new JsonScope(false, [])) , {headers, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

@Injectable()
export class ForumTopicCtrl {
  errorHandlerService: ErrorHandlerService;
  httpService: HttpClient;


  public constructor(httpService: HttpClient, errorHandlerService: ErrorHandlerService) {
    this.httpService = httpService;
    this.errorHandlerService = errorHandlerService;
  }

  public createObject(entity: BaseEntity): Observable<ForumTopic>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<ForumTopic>();
    this.httpService.post(environment.BASE_URL + '/api/forum-topic', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])) , {headers, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public deleteObject(id: string): Observable<void>  {
    const subject = new Subject<void>();
    this.httpService.delete<void>(environment.BASE_URL + '/api/forum-topic/' + id + '')
      .pipe(take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getCategoryTopics(arg0: string): Observable<ForumTopic[]>  {
    const subject = new Subject<ForumTopic[]>();
    this.httpService.get(environment.BASE_URL + '/api/forum-topic/category/' + arg0 + '', {responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getNewestTopics(arg0: string = "timestamp", arg1: boolean | null = true, arg2: number | null = 0, arg3: number | null = 20): Observable<ForumTopic[]>  {
    const queryParamsList: { name: string, value: string }[] = [];
    if (arg0 !== undefined && arg0 !== null) {
      queryParamsList.push({name: 'sortBy', value: arg0});
    }

    if (arg1 !== undefined && arg1 !== null) {
      queryParamsList.push({name: 'ascending', value: arg1.toString()});
    }

    if (arg2 !== undefined && arg2 !== null) {
      queryParamsList.push({name: 'pageNumber', value: arg2.toString()});
    }

    if (arg3 !== undefined && arg3 !== null) {
      queryParamsList.push({name: 'pageSize', value: arg3.toString()});
    }
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const subject = new Subject<ForumTopic[]>();
    this.httpService.get(environment.BASE_URL + '/api/forum-topic/get-newest', {params, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getObject(id: string): Observable<ForumTopic>  {
    const subject = new Subject<ForumTopic>();
    this.httpService.get(environment.BASE_URL + '/api/forum-topic/' + id + '', {responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateObject(entity: BaseEntity): Observable<ForumTopic>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<ForumTopic>();
    this.httpService.put(environment.BASE_URL + '/api/forum-topic/' + entity.id.split('/')[1] + '', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])) , {headers, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

@Injectable()
export class LanguageCtrl {
  errorHandlerService: ErrorHandlerService;
  httpService: HttpClient;


  public constructor(httpService: HttpClient, errorHandlerService: ErrorHandlerService) {
    this.httpService = httpService;
    this.errorHandlerService = errorHandlerService;
  }

  public findAll(): Observable<Language[]>  {
    const subject = new Subject<Language[]>();
    this.httpService.get(environment.BASE_URL + '/api/language', {responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getObject(id: string): Observable<Language>  {
    const subject = new Subject<Language>();
    this.httpService.get(environment.BASE_URL + '/api/language/' + id + '', {responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

@Injectable()
export class LoginCtrl {
  errorHandlerService: ErrorHandlerService;
  httpService: HttpClient;


  public constructor(httpService: HttpClient, errorHandlerService: ErrorHandlerService) {
    this.httpService = httpService;
    this.errorHandlerService = errorHandlerService;
  }

  public updateObject(entity: BaseEntity): Observable<BaseObject>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<BaseObject>();
    this.httpService.put(environment.BASE_URL + '/api/login/' + entity.id.split('/')[1] + '', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])) , {headers, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

@Injectable()
export class NotificationCtrl {
  errorHandlerService: ErrorHandlerService;
  httpService: HttpClient;


  public constructor(httpService: HttpClient, errorHandlerService: ErrorHandlerService) {
    this.httpService = httpService;
    this.errorHandlerService = errorHandlerService;
  }

  public createMailNotification(arg0: Mail, arg1: string, arg2: string): Observable<boolean>  {
    const queryParamsList: { name: string, value: string }[] = [];
    queryParamsList.push({name: 'receiverId', value: arg2});
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<boolean>();
    this.httpService.post<boolean>(environment.BASE_URL + '/api/notification/decline/' + arg1 + '/', arg0 , {headers, params})
      .pipe(take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public createNotificationWithMail(arg0: Notification, arg1: string, arg2: string): Observable<Notification>  {
    const queryParamsList: { name: string, value: string }[] = [];
    queryParamsList.push({name: 'sender', value: arg2});
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<Notification>();
    this.httpService.post(environment.BASE_URL + '/api/notification/' + arg1 + '/', JsonScopedSerializer.stringify(arg0, new JsonScope(false, [])) , {headers, params, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public createObject(entity: BaseEntity): Observable<Notification>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<Notification>();
    this.httpService.post(environment.BASE_URL + '/api/notification', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])) , {headers, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public deleteByVisitId(arg0: string): Observable<number>  {
    const subject = new Subject<number>();
    this.httpService.delete<number>(environment.BASE_URL + '/api/notification/visit/' + arg0 + '')
      .pipe(take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public deleteObject(id: string): Observable<void>  {
    const subject = new Subject<void>();
    this.httpService.delete<void>(environment.BASE_URL + '/api/notification/' + id + '')
      .pipe(take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateObject(entity: BaseEntity): Observable<Notification>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<Notification>();
    this.httpService.put(environment.BASE_URL + '/api/notification/' + entity.id.split('/')[1] + '', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])) , {headers, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

@Injectable()
export class PersonCtrl {
  errorHandlerService: ErrorHandlerService;
  httpService: HttpClient;


  public constructor(httpService: HttpClient, errorHandlerService: ErrorHandlerService) {
    this.httpService = httpService;
    this.errorHandlerService = errorHandlerService;
  }

  public countAll(): Observable<number>  {
    const subject = new Subject<number>();
    this.httpService.get<number>(environment.BASE_URL + '/api/person/countAll')
      .pipe(take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public deleteObject(arg0: string): Observable<void>  {
    const subject = new Subject<void>();
    this.httpService.delete<void>(environment.BASE_URL + '/api/person/' + arg0 + '')
      .pipe(take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getDoctorInfoFromPerson(arg0: string): Observable<Person>  {
    const subject = new Subject<Person>();
    this.httpService.get(environment.BASE_URL + '/api/person/get-doctor-info/' + arg0 + '', {responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getObject(id: string): Observable<Person>  {
    const subject = new Subject<Person>();
    this.httpService.get(environment.BASE_URL + '/api/person/' + id + '', {responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getPeople(sortBy?: string, ascending?: boolean | null, pageNumber?: number | null, pageSize?: number | null): Observable<Person[]>  {
    const queryParamsList: { name: string, value: string }[] = [];
    if (sortBy !== undefined && sortBy !== null) {
      queryParamsList.push({name: 'sortBy', value: sortBy});
    }

    if (ascending !== undefined && ascending !== null) {
      queryParamsList.push({name: 'ascending', value: ascending.toString()});
    }

    if (pageNumber !== undefined && pageNumber !== null) {
      queryParamsList.push({name: 'pageNumber', value: pageNumber.toString()});
    }

    if (pageSize !== undefined && pageSize !== null) {
      queryParamsList.push({name: 'pageSize', value: pageSize.toString()});
    }
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const subject = new Subject<Person[]>();
    this.httpService.get(environment.BASE_URL + '/api/person/getPeople', {params, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public isUsernameUsed(arg0: string): Observable<boolean>  {
    const queryParamsList: { name: string, value: string }[] = [];
    queryParamsList.push({name: 'username', value: arg0});
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const subject = new Subject<boolean>();
    this.httpService.get<boolean>(environment.BASE_URL + '/api/person/usage-username/', {params})
      .pipe(take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateObject(arg0: BaseEntity): Observable<Person>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<Person>();
    this.httpService.put(environment.BASE_URL + '/api/person/' + arg0.id.split('/')[1] + '', JsonScopedSerializer.stringify(arg0, new JsonScope(false, [])) , {headers, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

@Injectable()
export class RegistrationLinkCtrl {
  errorHandlerService: ErrorHandlerService;
  httpService: HttpClient;


  public constructor(httpService: HttpClient, errorHandlerService: ErrorHandlerService) {
    this.httpService = httpService;
    this.errorHandlerService = errorHandlerService;
  }

  public createRegistrationLinks(arg0: RegistrationLink[]): Observable<RegistrationLink[]>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<RegistrationLink[]>();
    this.httpService.post(environment.BASE_URL + '/api/registration-link/group', JsonScopedSerializer.stringify(arg0, new JsonScope(false, [])) , {headers, responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public deleteObject(id: string): Observable<void>  {
    const subject = new Subject<void>();
    this.httpService.delete<void>(environment.BASE_URL + '/api/registration-link/' + id + '')
      .pipe(take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getObject(id: string): Observable<RegistrationLink>  {
    const subject = new Subject<RegistrationLink>();
    this.httpService.get(environment.BASE_URL + '/api/registration-link/' + id + '', {responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getServerIp(): Observable<string>  {
    const subject = new Subject<string>();
    this.httpService.get<string>(environment.BASE_URL + '/api/registration-link/server/ip')
      .pipe(take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

@Injectable()
export class RoleCtrl {
  errorHandlerService: ErrorHandlerService;
  httpService: HttpClient;


  public constructor(httpService: HttpClient, errorHandlerService: ErrorHandlerService) {
    this.httpService = httpService;
    this.errorHandlerService = errorHandlerService;
  }

  public countAll(): Observable<number>  {
    const subject = new Subject<number>();
    this.httpService.get<number>(environment.BASE_URL + '/api/role/countAll')
      .pipe(take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public findAll(): Observable<Role[]>  {
    const subject = new Subject<Role[]>();
    this.httpService.get(environment.BASE_URL + '/api/role', {responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

@Injectable()
export class StaticTranslationCtrl {
  errorHandlerService: ErrorHandlerService;
  httpService: HttpClient;


  public constructor(httpService: HttpClient, errorHandlerService: ErrorHandlerService) {
    this.httpService = httpService;
    this.errorHandlerService = errorHandlerService;
  }

  public getObject(id: string): Observable<StaticTranslation>  {
    const subject = new Subject<StaticTranslation>();
    this.httpService.get(environment.BASE_URL + '/api/static-translation/' + id + '', {responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getStaticTranslations(languageLocale: string, functionalityName: string): Observable<StaticTranslation[]>  {
    const subject = new Subject<StaticTranslation[]>();
    this.httpService.get(environment.BASE_URL + '/api/static-translation/' + languageLocale + '/' + functionalityName + '', {responseType: 'text'})
      .pipe(map(res => JSON.parse(res)), take(1))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

