import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

const USER_SEARCH_API = 'http://localhost:8080/api/user/search/';

const httpOptions = {
  headers: new HttpHeaders({
    'content-Type': 'application/json'
  })
}

@Injectable({
  providedIn: 'root'
})
export class UserSearchService {

  constructor(private http: HttpClient, private route: ActivatedRoute) { }

  // Search for a user by Display Name
  public searchUsersByDisplayName(displayName: string): Observable<any> {
    return this.http.get(
      `${USER_SEARCH_API}byDisplayName?displayName=${displayName}`
    );
  }
}
