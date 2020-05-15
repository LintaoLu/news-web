import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { HomeComponent } from '../home/home.component';
import { auth } from 'firebase/app';
import 'firebase/auth';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {

  loginMode = true;
  authError = null;

  constructor(public auth: AuthService, private homeComponent: HomeComponent) {}

  ngOnInit(): void { }

  changeMode() {
    this.loginMode = !this.loginMode;
  }

  login(frm) {
    this.auth.login(frm.value.email, frm.value.password).then(
      () => { if (this.auth.isLogin) {
        this.homeComponent.closeModule();
        this.homeComponent.updateProfile();
      }}
    );

    this.auth.eventAuthError$.subscribe(
      (data) => { this.authError = data; }
    );
  }

  // Sign in with Google
  googleAuth() {
    this.auth.authLogin(new auth.GoogleAuthProvider()).then(
      () => { if (this.auth.isLogin) {
        this.homeComponent.closeModule();
        this.homeComponent.updateProfile();
      }}
    );
  } 
  
  facebookAuth() {
    this.auth.authLogin(new auth.FacebookAuthProvider()).then(
      () => { if (this.auth.isLogin) {
        this.homeComponent.closeModule();
        this.homeComponent.updateProfile();
      }}
    );
  }  

  twitterAuth() {
    this.auth.authLogin(new auth.TwitterAuthProvider()).then(
      () => { if (this.auth.isLogin) {
        this.homeComponent.closeModule();
        this.homeComponent.updateProfile();
      }}
    );
  }  
}