package org.launchcode.TasteBuddiesServer.models.dto;

import org.launchcode.TasteBuddiesServer.models.User;

import javax.servlet.http.HttpServletRequest;

public class FriendshipDTO {
    private int id;
    private FriendsDTO user;
    private FriendsDTO friend;
    private String status;

    public FriendshipDTO(FriendsDTO user, FriendsDTO friend, String status) {
        this.user = user;
        this.friend = friend;
        this.status = status;
    }

    public FriendshipDTO() {
    }

    public int getId() {
        return id;
    }

    public FriendsDTO getUser() {
        return user;
    }

    public void setUser(FriendsDTO user) {
        this.user = user;
    }

    public FriendsDTO getFriend() {
        return friend;
    }

    public void setFriend(FriendsDTO friend) {
        this.friend = friend;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
