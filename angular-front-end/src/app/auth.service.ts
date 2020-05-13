import { Injectable } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/auth';
import { AngularFirestore } from '@angular/fire/firestore';
import { Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  eventAuthError = new BehaviorSubject<string>("");
  eventAuthError$ = this.eventAuthError.asObservable();
  newUser: any;
  error: boolean;
  isLogin = false;

  constructor(private afAuth: AngularFireAuth, private db: AngularFirestore, private router: Router) { }

  public createrUser(user) {
    this.error = false;
    return this.afAuth.createUserWithEmailAndPassword(user.email, user.password)
    .then(userCredential => {
      this.newUser = user;
      
      userCredential.user.updateProfile({
        displayName: user.name
      });

      this.insertUserData(userCredential);
    })
    .catch( error => {
      this.error = true;
      this.eventAuthError.next(error);
    });
  }

  insertUserData(userCredential: firebase.auth.UserCredential) {
    return this.db.doc(`Users/${userCredential.user.uid}`).set({
      email: this.newUser.email,
      name: this.newUser.username,
    });
  }

  login(email: string, password: string) {
    return this.afAuth.signInWithEmailAndPassword(email, password).
    catch(error => { 
      this.error = true;
      this.eventAuthError.next(error)
    }).then( userCredential => {
      if (userCredential) {
        this.isLogin = true;
      }
    });
  }

  logout() {
    this.isLogin = false;
    return this.afAuth.signOut();
  }

  getUserState() {
    return this.afAuth.authState;
  }
}
