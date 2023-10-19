package org.launchcode.TasteBuddiesServer.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;

//    Pending, Accepted, or Rejected
    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    @ManyToOne
    @JoinColumn(name = "requested_by_id")
    private User requestedBy;

    private LocalDateTime establishedDate;

//    Action User is the user that initiated the last action (ex. sent a friend request)
    @ManyToOne
    @JoinColumn(name = "action_user_id")
    private User actionUser;

    public Friendship(User user1, User user2, FriendshipStatus status, User requestedBy, LocalDateTime establishedDate, User actionUser) {
        this.user1 = user1;
        this.user2 = user2;
        this.status = status;
        this.requestedBy = requestedBy;
        this.establishedDate = establishedDate;
        this.actionUser = actionUser;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    public User getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(User requestedBy) {
        this.requestedBy = requestedBy;
    }

    public LocalDateTime getEstablishedDate() {
        return establishedDate;
    }

    public void setEstablishedDate(LocalDateTime establishedDate) {
        this.establishedDate = establishedDate;
    }

    public User getActionUser() {
        return actionUser;
    }

    public void setActionUser(User actionUser) {
        this.actionUser = actionUser;
    }

    public enum FriendshipStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }
}
