package org.launchcode.TasteBuddiesServer.controllers;

import org.launchcode.TasteBuddiesServer.data.UserRepository;
import org.launchcode.TasteBuddiesServer.exception.UserNotFoundException;
import org.launchcode.TasteBuddiesServer.models.Friendship;
import org.launchcode.TasteBuddiesServer.models.User;
//import org.launchcode.TasteBuddiesServer.models.dto.CurrentUserDTO;
import org.launchcode.TasteBuddiesServer.models.dto.OtherUserDTO;
import org.launchcode.TasteBuddiesServer.services.FriendshipService;
import org.launchcode.TasteBuddiesServer.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/friendship")
@CrossOrigin(origins = "http://localhost:4200")
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

//    Send a Friend Request
    @PostMapping("/request")
    public ResponseEntity<?> sendFriendRequest(
            @RequestBody int friendId,
            HttpServletRequest request
            ) {
        User currentUser = userService.getUserFromRequest(request);
        User friend = userRepository.findById(friendId).orElseThrow(() -> new UserNotFoundException("Friend not Found"));
        System.out.println("Friend Request Attempt Made");

//        Validate that the friend request is not a duplicate
        if (friendshipService.isFriendRequestDuplicate(currentUser, friend)) {
            System.out.println("Friend Request Already Sent");
            return ResponseEntity.status(400).body("Friend request already sent.");
        }

        friendshipService.sendFriendRequest(currentUser, friend);
        System.out.println("Friend Request Attempt Made.");
        return ResponseEntity.status(200).body("Friend Request Sent.");
    }

//    Accept a Friend Request
    @PostMapping("/accept")
    public ResponseEntity<?> acceptFriendRequest(
            @RequestParam int friendId,
            HttpServletRequest request
    ) {
        User currentUser = userService.getUserFromRequest(request);
        User friend = userRepository.findById(friendId).orElseThrow(() -> new UserNotFoundException("Friend not Found"));

//        Check if pending friend request exists
        Friendship friendship = friendshipService.getPendingFriendship(currentUser, friend);
        if (friendship == null) {
            return ResponseEntity.status(404).body("Friend request not found.");
        }

        friendshipService.acceptFriendRequest(friendship);
        return ResponseEntity.status(200).body("Friend request accepted.");
    }

//    Reject a Friend Request
    @PostMapping("/reject")
    public  ResponseEntity<?> rejectFriendRequest(
            @RequestParam int friendId,
            HttpServletRequest request
    ) {
        User currentUser = userService.getUserFromRequest(request);
        User friend = userRepository.findById(friendId).orElseThrow(() -> new UserNotFoundException("Friend not Found."));

//        Check if pending friend request exists
        Friendship friendship = friendshipService.getPendingFriendship(currentUser, friend);
        if (friendship == null) {
            return ResponseEntity.status(404).body("Friend request not found.");
        }

        friendshipService.rejectFriendRequest(friendship);
        return ResponseEntity.status(200).body("Friend request rejected.");
    }

//    Get the user's friends list
    @GetMapping("/friends")
    public ResponseEntity<?> getFriendsList(
            @RequestParam int friendId,
            HttpServletRequest request
    ) {
        User currentUser = userService.getUserFromRequest(request);
        List<User> friends = friendshipService.getFriendsList(currentUser);

        if (friends.isEmpty()) {
            return ResponseEntity.status(204).body("No friends found.");
        }

        List<OtherUserDTO> friendDTOs = friends.stream()
                .map(OtherUserDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.status(200).body(friendDTOs);
    }
}
