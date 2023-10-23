package org.launchcode.TasteBuddiesServer.models.dto;

public class FriendshipDTO {
    private int id;
    private CurrentUserDTO user;
    private OtherUserDTO friend;
    private String status;

    public FriendshipDTO(int id, CurrentUserDTO user, OtherUserDTO friend, String status) {
        this.id = id;
        this.user = user;
        this.friend = friend;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public CurrentUserDTO getUser() {
        return user;
    }

    public void setUser(CurrentUserDTO user) {
        this.user = user;
    }

    public OtherUserDTO getFriend() {
        return friend;
    }

    public void setFriend(OtherUserDTO friend) {
        this.friend = friend;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
