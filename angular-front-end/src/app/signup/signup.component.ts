import { Component, OnInit } from '@angular/core';
import { LoginComponent } from '../login/login.component';
import { AuthService } from '../auth.service';
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
  // add NgbModalConfig and NgbModal to the component providers
  providers: [NgbModalConfig, NgbModal]
})
export class SignupComponent implements OnInit {
  authError = null;

  constructor(private loginComponent: LoginComponent, private auth: AuthService) { }

  ngOnInit(): void {
    this.auth.eventAuthError$.subscribe(
      (data) => { this.authError = data; }
    );
  }

  changeMode() {
    this.loginComponent.changeMode();
  }

  createUser(frm) {
    this.auth.createrUser(frm.value)
      .then(() => {if (!this.auth.error) {
        this.changeMode();
        this.auth.eventAuthError = new BehaviorSubject<string>("");
        this.auth.eventAuthError$ = this.auth.eventAuthError.asObservable();
      }});
  }
}
