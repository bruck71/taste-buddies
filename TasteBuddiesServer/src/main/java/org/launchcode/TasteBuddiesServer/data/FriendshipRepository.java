package org.launchcode.TasteBuddiesServer.data;

import org.launchcode.TasteBuddiesServer.models.Friendship;
import org.springframework.data.repository.CrudRepository;

public interface FriendshipRepository extends CrudRepository<Friendship, Integer> {

}
