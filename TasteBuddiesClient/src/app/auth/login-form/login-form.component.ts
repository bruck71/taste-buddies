import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Login } from 'src/models/login';
import { AuthenticationService } from 'src/services/authentication.service';
import { StorageService } from 'src/services/storage.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent implements OnInit {

  regModel: Login = new Login('', '') // 'nathan@example.net', 'password'
  submitted: boolean = false;
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];

  constructor(
    private authenticationService: AuthenticationService, 
    private storageService: StorageService,
    private router: Router,
    ) { }

  ngOnInit(): void {  }

  onSubmit(): void {
    this.authenticationService.login(this.regModel).subscribe({
      next: res => {
        this.storageService.saveJwt(res);
        this.roles = this.storageService.getJwt().roles;
        this.router.navigate(['/event']);
      },
      error: (e) => { 
        console.error(e.message)
        this.isLoginFailed = true;
      }
    });
  }

  reloadPage(): void {
    window.location.reload();
  }

}