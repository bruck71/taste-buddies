package org.launchcode.TasteBuddiesServer.models.dto;

import org.launchcode.TasteBuddiesServer.models.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EventDTO {
    private int id;
    private String entryCode;
    private String location;
    private String searchRadius;
    private String partySize;
    private String eventName;
    private CurrentUserDTO currentUser;
    private List<OtherUserDTO> otherUsers;
    private List<Restaurant> restaurants;
    private Date mealTime;

    private String mutuallyLikedRestaurant;

    public EventDTO() {
    }

    public EventDTO(int id,
                    String entryCode,
                    String location,
                    String searchRadius,
                    String partySize,
                    String eventName,
                    User currentUser,
                    List<User> otherUsers,
                    List<Restaurant> restaurants,
                    Date mealTime
    ) {
        this.id = id;
        this.entryCode = entryCode;
        this.location = location;
        this.searchRadius = searchRadius;
        this.partySize = partySize;
        this.eventName = eventName;
        this.currentUser = new CurrentUserDTO(currentUser);
        this.otherUsers = otherUsers
                .stream()
                .filter(user -> user.getId() != this.currentUser.getId())
                .map(OtherUserDTO::new)
                .collect(Collectors.toList());
        this.restaurants = restaurants;
        this.mealTime = mealTime;
    }

    public EventDTO(Event event, User currentUser) {
        this(
                event.getId(),
                event.getEntryCode(),
                event.getLocation(),
                event.getSearchRadius(),
                event.getPartySize(),
                event.getEventName(),
                currentUser,
                event.getUsers(),
                event.getAvailableRestaurants(),
                event.getMealTime()
        );

        this.restaurants = event.getAvailableRestaurants();
        this.mealTime = event.getMealTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSearchRadius() {
        return searchRadius;
    }

    public void setSearchRadius(String searchRadius) {
        this.searchRadius = searchRadius;
    }

    public String getPartySize() {
        return partySize;
    }

    public void setPartySize(String partySize) {
        this.partySize = partySize;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public CurrentUserDTO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(CurrentUserDTO currentUser) {
        this.currentUser = currentUser;
    }

    public List<OtherUserDTO> getOtherUsers() {
        return otherUsers;
    }

    public void setOtherUsers(List<OtherUserDTO> otherUsers) {
        this.otherUsers = otherUsers;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public Date getMealTime() {
        return mealTime;
    }

    public void setMealTime(Date mealTime) {
        this.mealTime = mealTime;
    }

    public String getMutuallyLikedRestaurant() { return mutuallyLikedRestaurant; }

    public void setMutuallyLikedRestaurant(String mutuallyLikedRestaurant) { this.mutuallyLikedRestaurant = mutuallyLikedRestaurant; }
}

