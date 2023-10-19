package org.launchcode.TasteBuddiesServer.data;

import org.launchcode.TasteBuddiesServer.models.Friendship;
import org.launchcode.TasteBuddiesServer.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendshipRepository extends CrudRepository<Friendship, Integer> {
    public List<Friendship> findByUser1AndStatus(User user, Friendship.FriendshipStatus status);
}
