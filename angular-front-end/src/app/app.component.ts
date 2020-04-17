import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { NewsService } from './news.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title = 'paper-web';
  news: any;

  constructor(private newsService: NewsService) { 
    newsService.getLatestNews().subscribe(
      (data) => this.news = data );
  }
}
