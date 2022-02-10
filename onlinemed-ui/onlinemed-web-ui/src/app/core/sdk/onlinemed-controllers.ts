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
import {map} from 'rxjs/operators';
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
    this.httpService.delete<void>(environment.BASE_UR + '/api/calendar-event/' + id + '')
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getUserEvents(id: string): Observable<CalendarEvent[]>  {
    const subject = new Subject<CalendarEvent[]>();
    this.httpService.get(environment.BASE_UR + '/api/calendar-event/user/' + id + '', {responseType: 'text'}).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public removeUserEvents(removeIdList: string[]): Observable<void>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<void>();
    this.httpService.post<void>(environment.BASE_UR + '/api/calendar-event/remove-many', removeIdList, {headers})
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateObject(entity: BaseEntity): Observable<CalendarEvent>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<CalendarEvent>();
    this.httpService.put(environment.BASE_UR + '/api/calendar-event/' + entity.id.split('/')[1] + '', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])), {
      headers,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateUserEvents(events: CalendarEvent[]): Observable<CalendarEvent[]>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<CalendarEvent[]>();
    this.httpService.post(environment.BASE_UR + '/api/calendar-event/update-many', JsonScopedSerializer.stringify(events, new JsonScope(false, [])), {
      headers,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
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
    this.httpService.get(environment.BASE_UR + '/api/community/' + id + '', {responseType: 'text'}).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateObject(entity: BaseEntity): Observable<Community>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<Community>();
    this.httpService.put(environment.BASE_UR + '/api/community/' + entity.id.split('/')[1] + '', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])), {
      headers,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
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
    this.httpService.get(environment.BASE_UR + '/api/doctor-info', {responseType: 'text'}).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateObject(entity: BaseEntity): Observable<DoctorInfo>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<DoctorInfo>();
    this.httpService.put(environment.BASE_UR + '/api/doctor-info/' + entity.id.split('/')[1] + '', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])), {
      headers,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
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

  public searchDrugHints(search: string): Observable<DrugHints[]>  {
    const queryParamsList: { name: string, value: string }[] = [];
    queryParamsList.push({name: 'search', value: search});
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const subject = new Subject<DrugHints[]>();
    this.httpService.get(environment.BASE_UR + '/api/drug-equivalent/hints', {
      params,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public searchDrugInfo(url: string): Observable<DrugInfo[]>  {
    const queryParamsList: { name: string, value: string }[] = [];
    queryParamsList.push({name: 'url', value: url});
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const subject = new Subject<DrugInfo[]>();
    this.httpService.get(environment.BASE_UR + '/api/drug-equivalent/drug-info', {
      params,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
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

  public sendMessageMail(mail: Mail, languageLocale: string, senderId: string, receiverId: string): Observable<boolean>  {
    const queryParamsList: { name: string, value: string }[] = [];
    queryParamsList.push({name: 'sender', value: senderId});

    queryParamsList.push({name: 'receiver', value: receiverId});
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<boolean>();
    this.httpService.post<boolean>(environment.BASE_UR + '/api/email/' + languageLocale + '/send', mail, {
      headers,
      params
    })
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
    this.httpService.get(environment.BASE_UR + '/api/forum-category', {responseType: 'text'}).pipe(map(res => JSON.parse(res)))
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
    this.httpService.post(environment.BASE_UR + '/api/forum-post', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])), {
      headers,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public deleteObject(id: string): Observable<void>  {
    const subject = new Subject<void>();
    this.httpService.delete<void>(environment.BASE_UR + '/api/forum-post/' + id + '')
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getPaginatedTopicPosts(topicId: string, pageNumber: number | null = 0, sortBy: string = "timestamp", ascending: boolean | null = true, pageSize: number | null = 10): Observable<ForumPost[]>  {
    const queryParamsList: { name: string, value: string }[] = [];
    if (pageNumber !== undefined && pageNumber !== null) {
      queryParamsList.push({name: 'pageNumber', value: pageNumber.toString()});
    }

    if (sortBy !== undefined && sortBy !== null) {
      queryParamsList.push({name: 'sortBy', value: sortBy});
    }

    if (ascending !== undefined && ascending !== null) {
      queryParamsList.push({name: 'ascending', value: ascending.toString()});
    }

    if (pageSize !== undefined && pageSize !== null) {
      queryParamsList.push({name: 'pageSize', value: pageSize.toString()});
    }
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const subject = new Subject<ForumPost[]>();
    this.httpService.get(environment.BASE_UR + '/api/forum-post/' + topicId + '/topic-posts', {
      params,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getTopicPostCount(topicId: string, pageNumber: number | null = 0, sortBy: string = "timestamp", ascending: boolean | null = true, pageSize: number | null = 10): Observable<number>  {
    const queryParamsList: { name: string, value: string }[] = [];
    if (pageNumber !== undefined && pageNumber !== null) {
      queryParamsList.push({name: 'pageNumber', value: pageNumber.toString()});
    }

    if (sortBy !== undefined && sortBy !== null) {
      queryParamsList.push({name: 'sortBy', value: sortBy});
    }

    if (ascending !== undefined && ascending !== null) {
      queryParamsList.push({name: 'ascending', value: ascending.toString()});
    }

    if (pageSize !== undefined && pageSize !== null) {
      queryParamsList.push({name: 'pageSize', value: pageSize.toString()});
    }
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const subject = new Subject<number>();
    this.httpService.get<number>(environment.BASE_UR + '/api/forum-post/' + topicId + '/topic-posts/count', {params})
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateObject(entity: BaseEntity): Observable<ForumPost>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<ForumPost>();
    this.httpService.put(environment.BASE_UR + '/api/forum-post/' + entity.id.split('/')[1] + '', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])), {
      headers,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
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
    this.httpService.post(environment.BASE_UR + '/api/forum-topic', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])), {
      headers,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public deleteObject(id: string): Observable<void>  {
    const subject = new Subject<void>();
    this.httpService.delete<void>(environment.BASE_UR + '/api/forum-topic/' + id + '')
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getCategoryTopics(id: string): Observable<ForumTopic[]>  {
    const subject = new Subject<ForumTopic[]>();
    this.httpService.get(environment.BASE_UR + '/api/forum-topic/category/' + id + '', {responseType: 'text'}).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getNewestTopics(sortBy: string = "timestamp", ascending: boolean | null = true, pageNumber: number | null = 0, pageSize: number | null = 20): Observable<ForumTopic[]>  {
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
    const subject = new Subject<ForumTopic[]>();
    this.httpService.get(environment.BASE_UR + '/api/forum-topic/get-newest', {
      params,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getObject(id: string): Observable<ForumTopic>  {
    const subject = new Subject<ForumTopic>();
    this.httpService.get(environment.BASE_UR + '/api/forum-topic/' + id + '', {responseType: 'text'}).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateObject(entity: BaseEntity): Observable<ForumTopic>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<ForumTopic>();
    this.httpService.put(environment.BASE_UR + '/api/forum-topic/' + entity.id.split('/')[1] + '', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])), {
      headers,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
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
    this.httpService.get(environment.BASE_UR + '/api/language', {responseType: 'text'}).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getObject(id: string): Observable<Language>  {
    const subject = new Subject<Language>();
    this.httpService.get(environment.BASE_UR + '/api/language/' + id + '', {responseType: 'text'}).pipe(map(res => JSON.parse(res)))
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
    this.httpService.put(environment.BASE_UR + '/api/login/' + entity.id.split('/')[1] + '', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])), {
      headers,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
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

  public createMailNotification(mail: Mail, languageLocale: string, receiverId: string): Observable<boolean>  {
    const queryParamsList: { name: string, value: string }[] = [];
    queryParamsList.push({name: 'receiverId', value: receiverId});
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<boolean>();
    this.httpService.post<boolean>(environment.BASE_UR + '/api/notification/decline/' + languageLocale + '/', mail, {
      headers,
      params
    })
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public createNotificationWithMail(entity: Notification, languageLocale: string, senderMail: string): Observable<Notification>  {
    const queryParamsList: { name: string, value: string }[] = [];
    queryParamsList.push({name: 'sender', value: senderMail});
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<Notification>();
    this.httpService.post(environment.BASE_UR + '/api/notification/' + languageLocale + '/', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])), {
      headers,
      params,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public createObject(entity: BaseEntity): Observable<Notification>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<Notification>();
    this.httpService.post(environment.BASE_UR + '/api/notification', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])), {
      headers,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public deleteByVisitId(visitId: string): Observable<number>  {
    const subject = new Subject<number>();
    this.httpService.delete<number>(environment.BASE_UR + '/api/notification/visit/' + visitId + '')
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public deleteObject(id: string): Observable<void>  {
    const subject = new Subject<void>();
    this.httpService.delete<void>(environment.BASE_UR + '/api/notification/' + id + '')
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateObject(entity: BaseEntity): Observable<Notification>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<Notification>();
    this.httpService.put(environment.BASE_UR + '/api/notification/' + entity.id.split('/')[1] + '', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])), {
      headers,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
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
    this.httpService.get<number>(environment.BASE_UR + '/api/person/countAll')
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public deleteObject(id: string): Observable<void>  {
    const subject = new Subject<void>();
    this.httpService.delete<void>(environment.BASE_UR + '/api/person/' + id + '')
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getDoctorInfoFromPerson(personId: string): Observable<Person>  {
    const subject = new Subject<Person>();
    this.httpService.get(environment.BASE_UR + '/api/person/get-doctor-info/' + personId + '', {responseType: 'text'}).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getObject(id: string): Observable<Person>  {
    const subject = new Subject<Person>();
    this.httpService.get(environment.BASE_UR + '/api/person/' + id + '', {responseType: 'text'}).pipe(map(res => JSON.parse(res)))
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
    this.httpService.get(environment.BASE_UR + '/api/person/getPeople', {
      params,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public isUsernameUsed(username: string): Observable<boolean>  {
    const queryParamsList: { name: string, value: string }[] = [];
    queryParamsList.push({name: 'username', value: username});
    let params = new HttpParams();
    for (const queryParam of queryParamsList) {
      params = params.append(queryParam.name, queryParam.value);
    }
    const subject = new Subject<boolean>();
    this.httpService.get<boolean>(environment.BASE_UR + '/api/person/usage-username/', {params})
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public updateObject(entity: BaseEntity): Observable<Person>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<Person>();
    this.httpService.put(environment.BASE_UR + '/api/person/' + entity.id.split('/')[1] + '', JsonScopedSerializer.stringify(entity, new JsonScope(false, [])), {
      headers,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
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

  public createRegistrationLinks(registrationLinks: RegistrationLink[]): Observable<RegistrationLink[]>  {
    const headers = new HttpHeaders().set('Content-type', 'application/json');
    const subject = new Subject<RegistrationLink[]>();
    this.httpService.post(environment.BASE_UR + '/api/registration-link/group', JsonScopedSerializer.stringify(registrationLinks, new JsonScope(false, [])), {
      headers,
      responseType: 'text'
    }).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public deleteObject(id: string): Observable<void>  {
    const subject = new Subject<void>();
    this.httpService.delete<void>(environment.BASE_UR + '/api/registration-link/' + id + '')
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getObject(id: string): Observable<RegistrationLink>  {
    const subject = new Subject<RegistrationLink>();
    this.httpService.get(environment.BASE_UR + '/api/registration-link/' + id + '', {responseType: 'text'}).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getServerIp(): Observable<string>  {
    const subject = new Subject<string>();
    this.httpService.get<string>(environment.BASE_UR + '/api/registration-link/server/ip')
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
    this.httpService.get<number>(environment.BASE_UR + '/api/role/countAll')
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public findAll(): Observable<Role[]>  {
    const subject = new Subject<Role[]>();
    this.httpService.get(environment.BASE_UR + '/api/role', {responseType: 'text'}).pipe(map(res => JSON.parse(res)))
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
    this.httpService.get(environment.BASE_UR + '/api/static-translation/' + id + '', {responseType: 'text'}).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

  public getStaticTranslations(languageLocale: string, functionalityName: string): Observable<StaticTranslation[]>  {
    const subject = new Subject<StaticTranslation[]>();
    this.httpService.get(environment.BASE_UR + '/api/static-translation/' + languageLocale + '/' + functionalityName + '', {responseType: 'text'}).pipe(map(res => JSON.parse(res)))
      .subscribe(res => subject.next(res), error => {
        this.errorHandlerService.handleErrors(error);
        subject.error(error);
      });
    return subject.asObservable();
  }

}

