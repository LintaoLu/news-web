import { Injectable, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class NewsService {
  
  constructor(private http: HttpClient) { }

  public getNews(type:string, id:number) {
    return this.http.get('http://157.245.242.4:8080/getNews?id=' + id + '&keyword=' + type);
  }
}
