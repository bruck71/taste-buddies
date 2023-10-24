package org.launchcode.TasteBuddiesServer.services;


import org.launchcode.TasteBuddiesServer.data.FriendshipRepository;
import org.launchcode.TasteBuddiesServer.data.UserRepository;
import org.launchcode.TasteBuddiesServer.exception.UserNotFoundException;
import org.launchcode.TasteBuddiesServer.models.Friendship;
import org.launchcode.TasteBuddiesServer.models.User;
import org.launchcode.TasteBuddiesServer.models.dto.FriendsDTO;
import org.launchcode.TasteBuddiesServer.models.dto.FriendshipDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class FriendshipService {
    @Autowired
    private final FriendshipRepository friendshipRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    public Friendship sendFriendRequest(FriendsDTO user1, FriendsDTO user2) {
        User sender = userRepository.findById(user1.getId()).orElseThrow(() -> new UserNotFoundException("Sender not found"));
        User receiver = userRepository.findById(user2.getId()).orElseThrow(() -> new UserNotFoundException("Sender not found"));

//        Create new Friendship Entity with status "Pending"
        Friendship friendship = new Friendship(
                sender,
                receiver,
                Friendship.FriendshipStatus.PENDING,
                sender,
                LocalDateTime.now(),
                sender
                );
        friendshipRepository.save(friendship);
        return friendship;
    }



//    Accept Friend Request (User 2 Accepts Request)
    public Friendship acceptFriendRequest(FriendshipDTO friendshipDTO) {
        FriendsDTO receiver = friendshipDTO.getUser();  // Receiver of friend request is now user
        FriendsDTO sender = friendshipDTO.getFriend();  // Sender of friend request is now friend

//        Get the User objects from the DTOs
        User senderUser = userRepository.findById(sender.getId()).orElseThrow(() -> new UserNotFoundException("Sender not found"));
        User receiverUser = userRepository.findById(receiver.getId()).orElseThrow(() -> new UserNotFoundException("Receiver not found"));
//        Find Pending friendship request in the repository
        Friendship friendship = friendshipRepository.findByUser1AndUser2AndStatus(senderUser, receiverUser, Friendship.FriendshipStatus.PENDING);
        if (friendship == null) {
            System.out.println("Friendship not found.");
            throw new UserNotFoundException("No Pending Friendships found.");
        }
//        Update the friendship status to ACCEPTED
        System.out.println("Friendship Found.");
        friendship.setStatus(Friendship.FriendshipStatus.ACCEPTED);
        friendship.setActionUser(friendship.getUser2()); //User 2 Accepts User 1's Friend Request
        friendship.setEstablishedDate(LocalDateTime.now());

        friendshipRepository.save(friendship);

        return friendship;
    }

//        Reject Friend Request (User 2 Accepts Request)
    public Friendship rejectFriendRequest(FriendshipDTO friendshipDTO) {
        FriendsDTO receiver = friendshipDTO.getUser();  // Receiver of friend request is now user
        FriendsDTO sender = friendshipDTO.getFriend();  // Sender of friend request is now friend

//        Get the User objects from the DTOs
        User senderUser = userRepository.findById(sender.getId()).orElseThrow(() -> new UserNotFoundException("Sender not found"));
        User receiverUser = userRepository.findById(receiver.getId()).orElseThrow(() -> new UserNotFoundException("Receiver not found"));

//        Find Pending friendship request in the repository

        Friendship friendship = friendshipRepository.findByUser1AndUser2AndStatus(senderUser, receiverUser, Friendship.FriendshipStatus.PENDING);
        if (friendship == null) {
            System.out.println("Friendship not found.");
            throw new UserNotFoundException("No Pending Friendships found.");
        }
//        Update the Friendship with Rejected
        System.out.println("Friendship Found.");
        friendship.setStatus(Friendship.FriendshipStatus.REJECTED);
        friendship.setActionUser(friendship.getUser2()); //User 2 Rejects User 1's Friend Request
        friendship.setEstablishedDate(LocalDateTime.now());

        friendshipRepository.save(friendship);

        return friendship;
    }

    public List<Friendship> getFriendships(FriendsDTO friendsDTO) {
        User user = userRepository.findById(friendsDTO.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        return friendshipRepository.findByUser1AndStatus(user, Friendship.FriendshipStatus.ACCEPTED); //Find Users with Status = Accepted
    }

    public List<FriendsDTO> getFriendsList(FriendsDTO friendsDTO) {
        List<Friendship> friendships = getFriendships(friendsDTO);

//        Create a list to store friends DTOs
        List<FriendsDTO> friends = new ArrayList<>();

        for (Friendship friendship : friendships) {
//            Determine which user in the friendship is the friend, and create a FriendsDTO.
            FriendsDTO friend;

            if (friendship.getUser1().getId() == friendsDTO.getId()) {
                friend = new FriendsDTO(friendship.getUser2().getId(), friendship.getUser2().getDisplayName());
            } else {
                friend = new FriendsDTO(friendship.getUser1().getId(), friendship.getUser1().getDisplayName());
            }
            friends.add(friend);
        }
        return friends;
    }

    public boolean isFriendRequestDuplicate(User sender, User receiver) {
        List<Friendship> friendships = friendshipRepository.findByUser1AndUser2(sender, receiver);

//      Check if there are existing friendships requests between these two users
        return friendships.stream().anyMatch(friendship -> friendship.getStatus() == Friendship.FriendshipStatus.PENDING);
    }
}
