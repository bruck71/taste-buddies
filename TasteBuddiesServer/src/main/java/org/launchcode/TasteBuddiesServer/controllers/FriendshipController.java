package org.launchcode.TasteBuddiesServer.controllers;

import org.launchcode.TasteBuddiesServer.data.UserRepository;
import org.launchcode.TasteBuddiesServer.exception.UserNotFoundException;
import org.launchcode.TasteBuddiesServer.models.Friendship;
import org.launchcode.TasteBuddiesServer.models.User;
//import org.launchcode.TasteBuddiesServer.models.dto.CurrentUserDTO;
import org.launchcode.TasteBuddiesServer.models.dto.CurrentUserDTO;
import org.launchcode.TasteBuddiesServer.models.dto.FriendsDTO;
import org.launchcode.TasteBuddiesServer.models.dto.FriendshipDTO;
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
            @RequestParam int friendId,
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
//        Create a  FriendshipDTO for Current User
        FriendsDTO sender = new FriendsDTO(
                currentUser.getId(),
                currentUser.getDisplayName()
        );

//        Create a  FriendshipDTO for Friend
        FriendsDTO receiver = new FriendsDTO(
                friend.getId(),
                friend.getDisplayName()
        );
//        Create a FriendshipDTO with CurrentUserDTO and OtherUserDTO
        FriendshipDTO friendshipDTO = new FriendshipDTO();
        friendshipDTO.setUser(sender);
        friendshipDTO.setFriend(receiver);
        friendshipDTO.setStatus("PENDING");

//         Use FriendshipDTO for service call
        friendshipService.sendFriendRequest(friendshipDTO.getUser(), friendshipDTO.getFriend());
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


        FriendshipDTO friendshipDTO = new FriendshipDTO(
                new FriendsDTO(currentUser.getId(), currentUser.getDisplayName()),
                new FriendsDTO(friend.getId(), friend.getDisplayName()),
                "PENDING"
        );
//          Use friendship service to check if pending friend request exists and accept friend request.
        friendshipService.acceptFriendRequest(friendshipDTO);

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

//        Use friendship service to check if pending friend request exists and reject friend
        FriendshipDTO friendshipDTO = new FriendshipDTO(
                new FriendsDTO(currentUser.getId(), currentUser.getDisplayName()),
                new FriendsDTO(friend.getId(), friend.getDisplayName()),
                "PENDING"
        );

        friendshipService.rejectFriendRequest(friendshipDTO);

        return ResponseEntity.status(200).body("Friend request rejected.");
    }

//    Get the user's friends list
    @GetMapping("/friends")
    public ResponseEntity<?> getFriendsList(HttpServletRequest request) {
        User currentUser = userService.getUserFromRequest(request);
        FriendsDTO user = new FriendsDTO(currentUser.getId(), currentUser.getDisplayName());

        List<FriendsDTO> friends = friendshipService.getFriendsList(user);

        if (friends.isEmpty()) {
            return ResponseEntity.status(204).body("No friends found.");
        }

        return ResponseEntity.status(200).body(friends);
    }
}
