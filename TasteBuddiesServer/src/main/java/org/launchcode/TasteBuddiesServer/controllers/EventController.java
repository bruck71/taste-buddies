package org.launchcode.TasteBuddiesServer.controllers;

import java.io.IOException;
import java.net.URISyntaxException;

import org.launchcode.TasteBuddiesServer.data.EventRepository;
import org.launchcode.TasteBuddiesServer.data.RestaurantRepository;
import org.launchcode.TasteBuddiesServer.data.UserRepository;
import org.launchcode.TasteBuddiesServer.models.Event;
import org.launchcode.TasteBuddiesServer.models.Restaurant;
import org.launchcode.TasteBuddiesServer.models.User;
import org.launchcode.TasteBuddiesServer.models.dto.*;
import org.launchcode.TasteBuddiesServer.models.geocode.Location;
import org.launchcode.TasteBuddiesServer.models.place.ResultsPlace;
import org.launchcode.TasteBuddiesServer.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/event")
@CrossOrigin(
        origins = "http://localhost:4200",
        allowCredentials = "true"
)
public class EventController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private NearbyService nearbyService;
    @Autowired
    private GeocodeService geocodeService;
    @Autowired
    private PlaceService placeService;

    @Value("${apiKey}")
    private String APIKey;

    @PostMapping("")
    public ResponseEntity<?> getEventFromId(
            @RequestBody int eventId,
            HttpServletRequest request
    ) {

        if (eventId <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        Optional<Event> possibleEvent = eventRepository.findById(eventId);

        if (possibleEvent.isEmpty()) {
            return ResponseEntity.status(204).body(null);
        }

        Optional<User> possibleCurrentUser = userService.getUserFromRequest(request);
        if (possibleCurrentUser.isEmpty()) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.status(200).body(new EventDTO(possibleEvent.get(), possibleCurrentUser.get()));
    }

    @GetMapping("all")
    public ResponseEntity<?> getAllEvents(HttpServletRequest request) {

        Optional<User> possibleCurrentUser = userService.getUserFromRequest(request);
        if (possibleCurrentUser.isEmpty()) {
            return ResponseEntity.status(403).build();
        }

        List<Event> events = eventRepository.findAllByUsers(possibleCurrentUser.get());

        if (events.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<EventDTO> allEvents = ((List<Event>) events)
                .stream()
                .map(event -> {
                    return new EventDTO(event, possibleCurrentUser.get());
                })
                .collect(Collectors.toList());

        return ResponseEntity.status(200).body(allEvents);
    }

    @PostMapping("create")
    public ResponseEntity<?> collectRestaurantData(
            @RequestBody CreateEventFormDTO createEventFormDTO,
            HttpServletRequest request
    )
            throws URISyntaxException, IOException, InterruptedException {


        Optional<User> possibleCurrentUser = userService.getUserFromRequest(request);
        if (possibleCurrentUser.isEmpty()) {
            return ResponseEntity.status(403).build();
        }

        Event newEvent = new Event(
                eventService.generateUniqueEntryCode(),
                createEventFormDTO.getLocation(),
                createEventFormDTO.getSearchRadius(),
                possibleCurrentUser.get(),
                createEventFormDTO.getMealTime()
        );

        Location location = geocodeService.getGeocodeFromAddress(createEventFormDTO.getLocation());
        List<String> placeIDs = nearbyService.getNearbyIDsFromLocationAndSearchRadius(location, createEventFormDTO.getSearchRadius());
        List<Restaurant> restaurants = new ArrayList<>();

        for (String placeID : placeIDs) {

            ResultsPlace resultsPlace = placeService.getRestaurantFromPlaceID(placeID);

            List<String> types = resultsPlace.getTypes();
//          todo: create else branch to put restaurant data in event even if it exists in restaurant repository
            if(!(types.contains("gas_station") || types.contains("convenience_store"))){
                if(!restaurantRepository.existsById(placeID)){
                    restaurantRepository
                            .save(new Restaurant(placeID, newEvent));
                }
                Optional<Restaurant> possibleRestaurant = restaurantRepository.findById(placeID);
                restaurants.add(possibleRestaurant.get());
            }

        }

        newEvent.setAvailableRestaurants(restaurants);
        eventRepository.save(newEvent);

        return ResponseEntity.status(200).build();
    }

    @PostMapping("join")
    public ResponseEntity<?> joinEvent(
            @RequestBody JoinEventDTO joinEventDTO,
            HttpServletRequest request
            ) throws URISyntaxException, IOException, InterruptedException {

        Optional<User> possibleUser = userService.getUserFromRequest(request);
        if(possibleUser.isEmpty()){
            return ResponseEntity.status(403).build();
        }

        Optional<Event> possibleEvent = eventRepository.findByEntryCode(joinEventDTO.getEntryCode());
        if(possibleEvent.isEmpty()){
            return ResponseEntity.status(403).build();
        }
        Event currentEvent = possibleEvent.get();
        List<User> moreUsers = currentEvent.getUsers();
        moreUsers.add(possibleUser.get());
        currentEvent.setUsers(moreUsers);
        eventRepository.save(currentEvent);
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/like")
    public ResponseEntity<?> likeRestaurant(
            @RequestBody UserLikesDTO userLikesDTO,
            HttpServletRequest request
            ) throws URISyntaxException, IOException, InterruptedException {
        System.out.println("Ran the event Id Controller");

        Optional<Event> possibleEvent = eventRepository.findById(userLikesDTO.getEventId());
        if (possibleEvent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Optional<User> possibleCurrentUser = userService.getUserFromRequest(request);
        if (possibleCurrentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userLikesDTO.setUserId(possibleCurrentUser.get().getId());

        // Process and save user likes within the event from the method in eventService
        eventService.saveLikedRestaurant(userLikesDTO);

        //Check if any restaurant has been liked by all users
        String mutuallyLikedRestaurant = eventService.getMutuallyLikedRestaurant(userLikesDTO);
        if (mutuallyLikedRestaurant != null) {
            System.out.println("Mutually Liked Restaurant: " + mutuallyLikedRestaurant);
            possibleEvent.get().setMutuallyLikedRestaurant(mutuallyLikedRestaurant);
            eventRepository.save(possibleEvent.get());
        } else {
            System.out.println("No Mutually Liked Restaurants");
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
