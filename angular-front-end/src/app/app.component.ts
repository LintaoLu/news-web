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
  type = 'general';
  id = 1;
  finished = false;
  isCollapsed = true;

  constructor(private newsService: NewsService) { }

  ngOnInit() {
    this.getNews();
  }

  onScroll() {
    this.getNews();
  }

  private getNews() {
    if (this.finished) return;

    this.newsService.getNews(this.type, this.id++).subscribe(
      data=> {
        let tmp = []; tmp = tmp.concat(data);
        for (let e of tmp) this.news.push({key:e, val:true});
        this.finished = tmp.length == 0 ? true : false;
      });
  }

  public reset(type: string) {
    this.news = [];
    this.type = type;
    this.id = 1;
    this.finished = false;
    this.getNews();
  }

  public backToTop() {
    window.scroll(0, 0);
  }
}
