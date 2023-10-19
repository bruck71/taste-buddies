package org.launchcode.TasteBuddiesServer.services;


import org.launchcode.TasteBuddiesServer.data.FriendshipRepository;
import org.launchcode.TasteBuddiesServer.models.Friendship;
import org.launchcode.TasteBuddiesServer.models.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

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

    public List<Friendship> getFriends(User user) {
        return friendshipRepository.findByUser1AndStatus(user, Friendship.FriendshipStatus.ACCEPTED); //Find Users with Status = Accepted
    }
}
