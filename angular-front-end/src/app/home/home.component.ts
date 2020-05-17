import { Component, OnInit } from '@angular/core';
import { NewsService } from '../news.service';
import { NgbModal, NgbModalConfig, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AuthService } from '../auth.service';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  // add NgbModalConfig and NgbModal to the component providers
  providers: [NgbModalConfig, NgbModal]
})
export class HomeComponent implements OnInit {
  news = [];
  source = [];
  type: string;
  id = 1;
  finished = false;
  isCollapsed = true;
  suggestions = [];
  mode = 'news';
  isLogin = false;
  userName = "Anonymous";
  modalReference: NgbModalRef;
  isNavbarCollapsed = true;

  constructor(private newsService: NewsService, config: NgbModalConfig, private modalService: NgbModal, 
    private auth: AuthService,private cookieService: CookieService) {
    // customize default values of modals used by this component tree
    config.backdrop = 'static';
    config.keyboard = false;
   }

  ngOnInit() {
    this.updateProfile();
    this.type = 'general';
    this.getNews();
    this.loadSource();
  }

  public getNews() {
    if (this.finished) return;

    this.newsService.getNews(this.type, this.id++).subscribe(
      data=> {
        let tmp = []; tmp = tmp.concat(data);
        for (let e of tmp) {
          this.news.push({key:e, isCollapsed:true, hasTweet:false, tweets:[], page:1});
        }
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
    this.isCollapsed = true;
    this.mode = 'news';
  }

  private loadSource() {
    this.newsService.getSource('a').subscribe(
      data=> {
        let tmp = []; tmp = tmp.concat(data);
        let i = 0;
        while (i < tmp.length) {
          let component = [];
          for (let j = 0; j < 5; j++) {
            component.push({sourceId: tmp[i].key, sourceName: tmp[i].value});
            if (++i == tmp.length) break;
          }
          this.source.push(component);
        }
        this.source.push([]);
      });
  }

  public getSuggestions(content: string) {
    this.newsService.getSuggestions(content).subscribe(
      data => {
        this.suggestions = [];
        this.suggestions = this.suggestions.concat(data);
      });
  }

  public changeMode(mode: string) {
    this.mode = mode;
  }

  open(content) {
    this.modalReference = this.modalService.open(content); 
  }

  closeModule() {
    this.modalReference.close();
  }

  updateProfile(){
    this.auth.getUserState().subscribe(
      user => {
        if (user) {
          this.userName = user.displayName;
          this.isLogin = true;
        }
      });
  }

  logout(){
    this.auth.logout();
    this.isLogin = false;
    this.userName = "Anonymous";
  }
}