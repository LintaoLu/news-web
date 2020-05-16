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
  hasError: boolean;
  isLogin = false;

  constructor(private afAuth: AngularFireAuth, private db: AngularFirestore, private router: Router) { }

  public createrUser(user) {
    this.hasError = false;
    return this.afAuth.createUserWithEmailAndPassword(user.email, user.password)
    .then(userCredential => {
      this.newUser = user;
      
      userCredential.user.updateProfile({
        displayName: user.username
      });

      this.insertUserData(userCredential);
    })
    .catch( error => {
      this.hasError = true;
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
      this.hasError = true;
      this.eventAuthError.next(error);
    }).then( userCredential => {
      if (userCredential) {
        this.isLogin = true;
      }
    });
  }

  logout() {
    this.afAuth.signOut().then(() => {this.isLogin = false;});
  }

  getUserState() {
    return this.afAuth.authState;
  }

  // Auth logic to run auth providers
  authLogin(provider) {
    return this.afAuth.signInWithPopup(provider)
    .then(() => {
      this.isLogin = true;
    }).catch((error) => {
      this.hasError = true;
      this.eventAuthError.next(error);
    })
  }

  getCurrentUser() {
    return this.afAuth.currentUser;
  }
}