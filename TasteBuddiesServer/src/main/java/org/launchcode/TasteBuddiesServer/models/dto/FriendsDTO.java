package org.launchcode.TasteBuddiesServer.models.dto;

public class FriendsDTO {
    private int id;
    private String displayName;
    private String email;

    public FriendsDTO(int id, String displayName, String email) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
