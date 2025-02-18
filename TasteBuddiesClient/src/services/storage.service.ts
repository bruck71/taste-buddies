import { Injectable } from '@angular/core';

const JWT_TOKEN = 'id_token';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

  constructor() { }

  clean(): void {
    window.localStorage.clear();
  }

  public saveJwt(jwt: any): void {
    window.localStorage.removeItem(JWT_TOKEN);
    window.localStorage.setItem(JWT_TOKEN, jwt.idToken);
  }

  public getJwt(): any | null {
    const token = window.localStorage.getItem(JWT_TOKEN);
    if (token) {
      return token;
    }

    return null;
  }

  public clearJwt(): void {
    window.localStorage.removeItem(JWT_TOKEN);
  }

  public isLoggedIn(): boolean {
    const token = window.localStorage.getItem(JWT_TOKEN);

    if (token == null) {
      return false;
    }

    return !this.isTokenExpired(token);
  }

  private isTokenExpired(token: any): boolean {
    const expiry = (JSON.parse(atob(token.split('.')[1]))).exp;

    let expired: boolean = (Math.floor((new Date).getTime() / 1000)) >= expiry;

    if (expired) {
      this.clearJwt();
      return true;
    }

    return false;
  }

}
