import { Component, OnInit } from '@angular/core';
import { LoginComponent } from '../login/login.component';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  constructor(private loginComponent: LoginComponent) { }

  ngOnInit(): void {}

  changeMode() {
    this.loginComponent.changeMode();
  }
}
