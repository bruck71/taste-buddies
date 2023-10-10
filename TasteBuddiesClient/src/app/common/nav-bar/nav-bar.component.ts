import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/services/authentication.service';
import { StorageService } from 'src/services/storage.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {

  buttons = [
    {buttonName : "Event", path : "event",},
  ];

  loginLogout = {
    buttonName: "Login", 
    path: "/login"
  };

  loggedIn: boolean = false;

  constructor(
    private storageService: StorageService, 
    private authenticationService: AuthenticationService,
    private router: Router) {}

  ngOnInit(): void {
    this.authenticationService.isLoggedIn$.subscribe(status => {
    this.loggedIn = status;
    //Update Navbar buttons based on login status
      if (this.loggedIn) {
        this.loginLogout = {buttonName : "Sign Out", path : "/"}
        this.buttons.push({buttonName : "Account", path : "account"})
      }
    })
  }

  logout(): void {
    this.authenticationService.logout();
    this.loggedIn = false;
    this.router.navigate(['/']);
  }
}
