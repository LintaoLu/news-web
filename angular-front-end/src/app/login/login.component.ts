import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {

  @Input() modalReference: any;

  loginMode = true;

  constructor() {}

  ngOnInit(): void { }

  changeMode() {
    this.loginMode = !this.loginMode;
  }
}
