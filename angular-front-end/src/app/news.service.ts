import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  constructor(private http: HttpClient) { }

  public getNews(type:string, id:number) {
    switch (type) {
      case 'general': return this.http.get('http://localhost:8080/getGeneralNews?id=' + id);
      case 'business': return this.http.get('http://localhost:8080/getBusinessNews?id=' + id);
      case 'entertainment': return this.http.get('http://localhost:8080/getEntertainmentNews?id=' + id);
      case 'health': return this.http.get('http://localhost:8080/getHealthNews?id=' + id);
      case 'science': return this.http.get('http://localhost:8080/getScienceNews?id=' + id);
      case 'sports': return this.http.get('http://localhost:8080/getSportsNews?id=' + id);
      case 'technology': return this.http.get('http://localhost:8080/getTechnologyNews?id=' + id);
      default: return null;
    }
  }
}
