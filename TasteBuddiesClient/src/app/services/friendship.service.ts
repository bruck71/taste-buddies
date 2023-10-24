import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';


const FRIENDSHIP_API = 'http://localhost:8080/api/friendship/';

const httpOptions = {
  headers: new HttpHeaders({
    'content-Type': 'application/json'
  })
}

@Injectable({
  providedIn: 'root'
})
export class FriendshipService {

  constructor(private http: HttpClient, private route: ActivatedRoute) { }

  // Send a friend Request
  public sendFriendRequest(friendId: number): Observable<any> {
    return this.http.post(
      `${FRIENDSHIP_API}request?friendId=${JSON.stringify(friendId)}`,
      null,
      httpOptions
    );
  }

  // Accept a friend request
  public acceptFriendRequest(friendId: number): Observable<any> {
    return this.http.post(
      `${FRIENDSHIP_API}accept?friendId=${JSON.stringify(friendId)}`,
      null,
      httpOptions
    );
  }

  // Reject a friend request
  public rejectFriendRequest(friendId: number): Observable<any> {
    return this.http.post(
      `${FRIENDSHIP_API}reject?friendId=${JSON.stringify(friendId)}`,
      null,
      httpOptions
    );
  }

  // Get friends list
  public getFriendsList(friendId: number): Observable<any> {
    return this.http.get(
      `${FRIENDSHIP_API}friends`,
      httpOptions
    );
  }
}
