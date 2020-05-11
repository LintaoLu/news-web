import { Component, OnInit, Input } from '@angular/core';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {

  @Input() login: boolean;

  authError: any;

  constructor(private auth: AuthService) {}

  ngOnInit(): void {
  }

  createUser(frm) {
    this.auth.createrUser(frm.value);
  }
}
