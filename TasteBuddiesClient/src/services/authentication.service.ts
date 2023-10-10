import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Login } from 'src/models/login';
import { Registration } from 'src/models/registration';
import { StorageService } from './storage.service';

const AUTH_API = 'http://localhost:8080/api/auth/'

const httpOptions = {
  headers: new HttpHeaders({
     'Content-Type': 'application/json' 
    })
};

@Injectable({
  providedIn: 'root'
})

export class AuthenticationService {

  private isLoggedInSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

  //Expose an observable to subscribe to the login status changes
  isLoggedIn$ = this.isLoggedInSubject.asObservable();

  constructor(private http: HttpClient, private storageService: StorageService) {
    //Initialize the isLoggedIn$ observable based on the stored login status
    this.isLoggedInSubject.next(this.storageService.isLoggedIn()); 
  }

  login(credentials: Login): Observable<any> {
    //Update isLoggedIn to true once user is logged in
    this.isLoggedInSubject.next(true);
    return this.http.post(
      AUTH_API + 'authenticate', 
      JSON.stringify(credentials),
      httpOptions,
    );
  }

  register(data: Registration): Observable<any> {
    console.log(JSON.stringify(data))
    return this.http.post(
      AUTH_API + 'register',
      JSON.stringify(data),
      httpOptions,
    );
  }

  logout(): void {
    this.storageService.clearJwt();
    //Update isLoggedIn to false once user is logged out
    this.isLoggedInSubject.next(false);
  }

  isAuthenticated(): boolean {
    return this.storageService.isLoggedIn();
  }
  
}
