import { Injectable, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class NewsService {

  ipAddress:string;
  
  constructor(private http: HttpClient) { 
    this.getIPAddress();
  }

  getIPAddress(){
    this.http.get("http://api.ipify.org/?format=json").subscribe((res:any)=>{
      this.ipAddress = res.ip;
    });
  }

  public getNews(type:string, id:number) {
    return this.http.get('http://localhost:8080/getNews?id=' + id + '&ip=' + this.ipAddress + '&keyword=' + type);
  }
}
