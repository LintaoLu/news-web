import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  constructor(private http: HttpClient) { }

  ip = window.location.origin;

  public getNews(type:string, id:number) {
    return this.http.get('http://161.35.49.253:8080/getNews?id=' + id + '&ip=' + this.ip + '&keyword=' + type);
  }
}
