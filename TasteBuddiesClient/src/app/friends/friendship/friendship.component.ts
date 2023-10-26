import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FriendshipService } from 'src/app/services/friendship.service';
import { UserSearchService } from 'src/app/services/user-search.service';
import { FriendsDTO } from 'src/models/DTO/friends-dto';
import { User } from 'src/models/user';

@Component({
  selector: 'app-friendship',
  templateUrl: './friendship.component.html',
  styleUrls: ['./friendship.component.css']
})
export class FriendshipComponent implements OnInit {
  searchResults: User[] = [];  //Variable to store search results
  searchQuery: string = ''; // Variable to store the search query

  constructor(
    private router: Router,
    private friendshipService: FriendshipService,
    private userSearchService: UserSearchService,
  ) { }

  ngOnInit(): void {
  }

  // Send Friend Requests - Called when Send Request button is clicked.
  sendFriendRequest(userId: number): void {
    this.friendshipService.sendFriendRequest(userId).subscribe({
      next: res => {
        console.log('Friend request sent successfully.');
      },
      error: (error) => {
        console.error('Error sending friend request:', error);
      },
    })
  }


  // Accept Friend Requests

  // Reject Friend Requests

  // Search for Users
  // searchUsers(): void {
  //   if (this.searchQuery.trim() === '') {
  //     return; // Don't search with empty query
  //   }

  onSubmit(): void {
    // Call the user search service
    this.userSearchService.searchUsersByDisplayName(this.searchQuery).subscribe({
      next: res => {
        console.log('Search results:', res);
        this.searchResults = res;
      },
      error: (error) => {
        console.error('Search error:', error);
      },
    });
  } 
}
