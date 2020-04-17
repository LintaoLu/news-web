import { Component, OnInit } from '@angular/core';
import { NewsService } from './news.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit{
  title = 'paper-web';
  news = [];
  id = 1;
  size = 0;
  finished = false;
  test = 0;

  constructor(private newsService: NewsService) { }

  ngOnInit() {
    this.getNews();
  }

  onScroll() {
    this.getNews();
  }

  private getNews() {
    if (this.finished) return;

    let len: number;
    this.newsService.getLatestNews(this.id++).subscribe(
      data=> {
        this.news = this.news.concat(data); 
        this.finished = this.news.length == this.size ? true : false;
        this.size = this.news.length;
      });
  }
}
