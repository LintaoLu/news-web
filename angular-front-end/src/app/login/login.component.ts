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
  message: string;
  timeLeft: number;
  interval;
  startCountDown = false;

  constructor(public auth: AuthService, private homeComponent: HomeComponent) {}

  ngOnInit(): void { }

  changeMode() {
    this.authError = null;
    this.loginMode = !this.loginMode;
  }

  login(frm) {
    this.message = null;
    this.authError = null;
    this.auth.login(frm.value.email, frm.value.password).then(
      () => { if (this.auth.isLogin) {
        this.auth.getCurrentUser().then(u => { 
          if (u.emailVerified) {
            this.homeComponent.closeModule();
            this.homeComponent.updateProfile();
          }
          else if (!u.emailVerified){
            this.authError = null;
            this.auth.logout();
            this.message = "Please verify your email!";
          }
        });
      }}
    );

    this.auth.eventAuthError$.subscribe(
      (data) => { this.authError = data; }
    );
  }

  sendEmail(){
    this.auth.getCurrentUser().then( u => {
      u.sendEmailVerification();
      this.timeLeft = 60;
      this.startCountDown = true;
      this.startTimer();
    });
  }

  startTimer() {
    this.interval = setInterval(() => {
      if(this.timeLeft > 0) {
        this.timeLeft--;
      } else {
        this.timeLeft = 60;
        this.startCountDown = false;
      }
    },1000);
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