import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { HomeComponent } from '../home/home.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {

  loginMode = true;
  authError = null;
  user: firebase.User;

  constructor(private auth: AuthService, private homeComponent: HomeComponent) {}

  ngOnInit(): void { 
    this.auth.getUserState().subscribe(
      user => {
        this.user = user;
      }
    );
    this.auth.eventAuthError$.subscribe(
      (data) => { this.authError = data; }
    );
  }

  changeMode() {
    this.loginMode = !this.loginMode;
  }

  login(frm) {
    this.auth.login(frm.value.email, frm.value.password).then(
      () => { if (this.auth.isLogin) {
        this.homeComponent.closeModule();
        this.homeComponent.updateProfile(this.user);
      }}
    );
  }
}