import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {

  authError: any;
  loginMode = true;

  constructor(private auth: AuthService) {}

  ngOnInit(): void { }

  createUser(frm) {
    this.auth.createrUser(frm.value);
  }

  changeMode() {
    this.loginMode = !this.loginMode;
  }
}
