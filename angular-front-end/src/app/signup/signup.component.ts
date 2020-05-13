import { Component, OnInit } from '@angular/core';
import { LoginComponent } from '../login/login.component';
import { AuthService } from '../auth.service';
import { NgbModalConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css'],
  // add NgbModalConfig and NgbModal to the component providers
  providers: [NgbModalConfig, NgbModal]
})
export class SignupComponent implements OnInit {
  authError = null;
  error = null;

  constructor(private loginComponent: LoginComponent, private auth: AuthService) { }

  ngOnInit(): void { }

  changeMode() {
    this.loginComponent.changeMode();
  }

  createUser(frm) {
    let pass = frm.value.password;
    let confirmPassword = frm.value.confirmPassword;
    if (pass !== confirmPassword) {
      this.error = "Your password and confirmation password do not match."
      return;
    }

    this.error = null;
    this.auth.createrUser(frm.value)
      .then(() => {if (!this.auth.error) {
        this.changeMode();
      }});

    this.auth.eventAuthError$.subscribe(
      (data) => { this.authError = data; }
    );
  }
}
