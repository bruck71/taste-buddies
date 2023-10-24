import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FriendshipService } from 'src/app/services/friendship.service';
import { UserSearchService } from 'src/app/services/user-search.service';
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

  // Send Friend Requests

  // Accept Friend Requests

  // Reject Friend Requests

  // Search for Users
  searchUsers(): void {
    if (this.searchQuery.trim() === '') {
      return; // Don't search with empty query
    }

    // Call the user search service
    this.userSearchService.searchUsersByDisplayName(this.searchQuery).subscribe(
      (results: User[]) => {
        this.searchResults = results; //Store the search results
      },
      (error) => {
        console.error('Error:', error);
      }
    )
  }

}
