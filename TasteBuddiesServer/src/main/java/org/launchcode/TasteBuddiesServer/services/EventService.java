package org.launchcode.TasteBuddiesServer.services;

import org.launchcode.TasteBuddiesServer.data.*;
import org.launchcode.TasteBuddiesServer.models.Restaurant;
import org.launchcode.TasteBuddiesServer.models.UserLikes;
import org.launchcode.TasteBuddiesServer.models.dto.CreateEventFormDTO;
import org.launchcode.TasteBuddiesServer.models.Event;
import org.launchcode.TasteBuddiesServer.models.User;
import org.launchcode.TasteBuddiesServer.models.dto.EventDTO;
import org.launchcode.TasteBuddiesServer.models.dto.UserLikesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserLikesRepository userLikesRepository;

    @Autowired
    private UserDislikesRepository userDislikesRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public static final char[] UPPERCASE_LETTERS = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    public static final int ENTRY_CODE_LENGTH = 6;

    public Event createEvent(CreateEventFormDTO request, String userEmail) {
        Event event = new Event();
        event.setLocation(request.getLocation());
        event.setSearchRadius(request.getSearchRadius());

        String entryCode = generateUniqueEntryCode();
        event.setEntryCode(entryCode);

        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            event.getUsers().add(user);

            Event savedEvent = eventRepository.save(event);
            user.getEvents().add(savedEvent);
            userRepository.save(user);

            return savedEvent;
        } else {
            // handel the case when user is not found, throw exception or return default value
            return event;
        }

    }

    public String generateUniqueEntryCode() {
        //Implement unique entry

        String roomCode;
        StringBuilder codeBuilder = new StringBuilder();

        while (true) {

            /*
            This is a pretty simple approach to code generation, but there are approximately
            300 million possible combinations. So for now this is a fine way to generate codes.
            I think the design would eventually need to change so these codes expire.
             */
            for (int i = 0; i < ENTRY_CODE_LENGTH; i++) {
                int random = (int) Math.floor(Math.random() * 26);
                char letter = UPPERCASE_LETTERS[random];
                codeBuilder.append(letter);
            }

            roomCode = codeBuilder.toString();

            Optional possibleEvent = eventRepository.findByEntryCode(roomCode);

            if (possibleEvent.isEmpty()) {
                break;
            }

        }
        return roomCode;
    }

    public void saveLikedRestaurant(UserLikesDTO userLikesDTO) {
        // Retrieve the user, event, and restaurant IDs from the DTO
        Integer userId = userLikesDTO.getUserId();
        Integer eventId = userLikesDTO.getEventId();
        String restaurantId = userLikesDTO.getRestaurantId();


        System.out.println("UserID: " + userId);
        System.out.println("EventID: " + eventId);
        System.out.println("Restaurant ID: " + restaurantId);

        // Retrieve the User and Event objects based on the provided IDs
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("Event not found"));

        // Fetch the UserLikes entity based on the provided user and event IDs
        Optional<UserLikes> userLikesOptional = userLikesRepository.findByUserAndEvent(user, event);
        UserLikes userLikes;

        //Check if userLikes exists.  If not create it and pull it from database by using createuserlikes method
        if (userLikesOptional.isEmpty()) {
            userLikesOptional = this.createUserLikes(user, event);
        }
            // If UserLikes entity exists, update the liked restaurants
            userLikes = userLikesOptional.get();
            List<Restaurant> likedRestaurants = userLikes.getLikedRestaurants();
            // Check if the restaurant already exists in the likedRestaurant list
            boolean restaurantExists = likedRestaurants.stream()
                    .anyMatch(restaurant -> restaurant.getId().equals(restaurantId));
            if (!restaurantExists) {
                Restaurant restaurantToAdd = new Restaurant();
                restaurantToAdd.setId(restaurantId);

                // Add the restaurant to the liked restaurants list
                //Save to repository
                userLikes.getLikedRestaurants().add(restaurantToAdd);
                userLikesRepository.save(userLikes);
                System.out.println("User Likes Data: " + userLikes);
                }
            }
        //Create User Likes object
        public Optional<UserLikes> createUserLikes(User user, Event event) {
             UserLikes userLikes = new UserLikes(user, event);
             userLikesRepository.save(userLikes);
             return userLikesRepository.findByUserAndEvent(user, event);
        }
}
