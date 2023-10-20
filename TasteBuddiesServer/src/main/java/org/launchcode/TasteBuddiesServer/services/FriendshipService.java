package org.launchcode.TasteBuddiesServer.services;


import org.launchcode.TasteBuddiesServer.data.FriendshipRepository;
import org.launchcode.TasteBuddiesServer.models.Friendship;
import org.launchcode.TasteBuddiesServer.models.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FriendshipService {

    private final FriendshipRepository friendshipRepository;

    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }
//    Send Friend Request (User 1 Sends User 2 Request)
    public Friendship sendFriendRequest(User sender, User receiver) {
//        Create a new Friendship entity with status "Pending"
        Friendship friendship = new Friendship(
                sender,
                receiver,
                Friendship.FriendshipStatus.PENDING,
                sender, LocalDateTime.now(),
                sender
        );
        return friendshipRepository.save(friendship);
    }

//    Accept Friend Request (User 2 Accepts Request)
    public Friendship acceptFriendRequest(Friendship friendship) {
        friendship.setStatus(Friendship.FriendshipStatus.ACCEPTED);
        friendship.setActionUser(friendship.getUser2()); //User 2 Accepts User 1's Friend Request
        return friendshipRepository.save(friendship);
    }

//        Reject Friend Request (User 2 Accepts Request)
    public Friendship rejectFriendRequest(Friendship friendship) {
        friendship.setStatus(Friendship.FriendshipStatus.REJECTED);
        friendship.setActionUser(friendship.getUser2()); //User 2 Rejects User 1's Friend Request
        return friendshipRepository.save(friendship);
    }

    public List<Friendship> getFriendships(User user) {
        return friendshipRepository.findByUser1AndStatus(user, Friendship.FriendshipStatus.ACCEPTED); //Find Users with Status = Accepted
    }

    public List<User> getFriendsList(User user) {
        List<Friendship> friendships = getFriendships(user);

        List<User> friends = friendships.stream()
                .map(friendship -> friendship.getUser1().equals(user) ? friendship.getUser2() : friendship.getUser1())
                .collect(Collectors.toList());

        return friends;
    }

    public boolean isFriendRequestDuplicate(User sender, User receiver) {
        List<Friendship> friendships = friendshipRepository.findByUser1AndUser2(sender, receiver);

//      Check if there are existing friendships requests between these two users
        return friendships.stream().anyMatch(friendship -> friendship.getStatus() == Friendship.FriendshipStatus.PENDING);
    }

    public Friendship getPendingFriendship(User sender, User receiver) {
        List<Friendship> friendships = friendshipRepository.findByUser1AndStatus(sender, Friendship.FriendshipStatus.PENDING);

//        Find a pending friendship where the second user is the receiver.
        Optional<Friendship> pendingFriendship = friendships.stream()
                .filter(friendship -> friendship.getUser2().equals(receiver))
                .findFirst();
        return pendingFriendship.orElse(null);
    }
}
