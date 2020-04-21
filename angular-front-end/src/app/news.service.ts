import { Injectable, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class NewsService {
  
  constructor(private http: HttpClient) { }
s
  public getNews(type:string, id:number) {
    return this.http.get('https://161.35.49.253:8443/getNews?id=' + id + '&keyword=' + type);
  }
}
