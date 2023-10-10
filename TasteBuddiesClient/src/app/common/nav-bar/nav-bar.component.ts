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
    {buttonName : "Event", path : "event",}
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
        // Check if the user is logged in
        this.loggedIn = this.storageService.isLoggedIn();

        //Add/Change Buttons if user is logged in
        if (this.loggedIn) {
        this.loginLogout = {buttonName : "Sign Out", path : "/"}
        this.buttons.push({buttonName : "Account", path : "account"})
      }
    }

  // ngOnInit(): void {
  // }

  logout(): void {
    this.authenticationService.logout();
    this.loggedIn = false;
    this.router.navigate(['/']);
  }
}
