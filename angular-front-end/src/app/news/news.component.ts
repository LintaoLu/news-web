import { Component, OnInit, Input } from '@angular/core';
import { NewsService } from '../news.service';
import { HomeComponent } from '../home/home.component';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.css']
})
export class NewsComponent implements OnInit {

  @Input() news: any;
  @Input() finished: boolean;

  pageSize = 3;
  
  constructor(private newsService: NewsService, private home: HomeComponent) { }

  ngOnInit(): void { }

  public loadTweets(e:any) {
    if (e.hasTweet) return;
    this.newsService.getTweets(e.key.title).subscribe(
      data=> {
        e.tweets = e.tweets.concat(data);
        e.hasTweet = true;
      });
  }

  public goToLink(url: string){
    window.open(url);
  }

  onScroll() {
    this.home.getNews();
  }

  backToTop() {
    window.scroll(0, 0);
  }
}
