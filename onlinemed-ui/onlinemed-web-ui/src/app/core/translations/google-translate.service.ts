import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GoogleTranslateService {
  private readonly url = 'https://translate.googleapis.com/translate_a/single';
  private readonly headers = new HttpHeaders().set('user-agent', 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36');

  constructor(private httpClient: HttpClient) {
    this.headers.append('Access-Control-Allow-Origin', '*');
  }

  public translate(fromLanguage: string, toLanguage: string, textToTranslate: string): Observable<any> {
    let params = new HttpParams();
    params = params.append('client', 'gtx');
    params = params.append('sl', fromLanguage);
    params = params.append('tl', toLanguage);
    params = params.append('dt', 't');
    params = params.append('q', textToTranslate);
    return this.httpClient.get(this.url, {headers: this.headers, params});
  }
}
