import { Component, OnInit } from '@angular/core';
import { NewsService } from '../news.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  title = 'paper-web';
  news = [];
  type: string;
  id = 1;
  finished = false;

  constructor(private newsService: NewsService) { }

  ngOnInit() {
    this.type = localStorage.getItem('type');
    if (this.type === null)  this.type = 'general';
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
        localStorage.setItem('type', this.type);
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
