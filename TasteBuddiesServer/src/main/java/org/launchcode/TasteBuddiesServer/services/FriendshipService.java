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
        FriendsDTO sender = friendshipDTO.getUser();
        FriendsDTO receiver = friendshipDTO.getFriend();

//        Find Pending friendship request in the repository
        Friendship friendship = friendshipRepository.findByUser1AndUser2AndStatus(sender.getId(), receiver.getId(), Friendship.FriendshipStatus.PENDING);
        if (friendship == null) {
            throw new UserNotFoundException("No Pending Friendships found.");
        }
//        Update the friendship status to ACCEPTED
        friendship.setStatus(Friendship.FriendshipStatus.ACCEPTED);
        friendship.setActionUser(friendship.getUser2()); //User 2 Accepts User 1's Friend Request
        friendship.setEstablishedDate(LocalDateTime.now());

        friendshipRepository.save(friendship);

        return friendship;
    }

//        Reject Friend Request (User 2 Accepts Request)
    public Friendship rejectFriendRequest(FriendshipDTO friendshipDTO) {
        FriendsDTO sender = friendshipDTO.getUser();
        FriendsDTO receiver = friendshipDTO.getFriend();

//        Find Pending friendship request in the repository

        Friendship friendship = friendshipRepository.findByUser1AndUser2AndStatus(sender.getId(), receiver.getId(), Friendship.FriendshipStatus.PENDING);
        if (friendship == null) {
            throw new UserNotFoundException("No Pending Friendships found.");
        }
//        Update the Friendship with Rejected
        friendship.setStatus(Friendship.FriendshipStatus.REJECTED);
        friendship.setActionUser(friendship.getUser2()); //User 2 Rejects User 1's Friend Request
        friendship.setEstablishedDate(LocalDateTime.now());

        friendshipRepository.save(friendship);

        return friendship;
    }

    public List<Friendship> getFriendships(FriendsDTO friendsDTO) {

        return friendshipRepository.findByUser1AndStatus(friendsDTO.getId(), Friendship.FriendshipStatus.ACCEPTED); //Find Users with Status = Accepted
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

    public Friendship getPendingFriendship(User sender, User receiver) {
        List<Friendship> friendships = friendshipRepository.findByUser1AndStatus(sender.getId(), Friendship.FriendshipStatus.PENDING);

//        Find a pending friendship where the second user is the receiver.
        Optional<Friendship> pendingFriendship = friendships.stream()
                .filter(friendship -> friendship.getUser2().equals(receiver))
                .findFirst();
        return pendingFriendship.orElse(null);
    }

    public FriendsDTO createFriendsDTOFromUserId(int userId) {
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            FriendsDTO friendDTO = new FriendsDTO(user.getId(), user.getDisplayName());
            return friendDTO;
        } else {
            return null;
        }
    }
}
