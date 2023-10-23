package org.launchcode.TasteBuddiesServer.data;

import org.launchcode.TasteBuddiesServer.models.Friendship;
import org.launchcode.TasteBuddiesServer.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendshipRepository extends CrudRepository<Friendship, Integer> {
    public List<Friendship> findByUser1AndStatus(int userId, Friendship.FriendshipStatus status);

    public List<Friendship> findByUser1AndUser2(User user1, User user2);

    public Friendship findByUser1AndUser2AndStatus(int senderId, int receiverId, Friendship.FriendshipStatus status);
}
