package org.launchcode.TasteBuddiesServer.controllers;

import org.launchcode.TasteBuddiesServer.data.UserRepository;
import org.launchcode.TasteBuddiesServer.models.User;
import org.launchcode.TasteBuddiesServer.models.dto.OtherUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/search")
@CrossOrigin(origins = "http://localhost:4200")
public class UserSearchController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/byDisplayName")
    public ResponseEntity<?> searchUsersByDisplayName(@RequestParam String displayName) {
        List<User> users = userRepository.findByDisplayNameContaining(displayName);
        if (users.isEmpty()) {
            return ResponseEntity.status(204).body(null); // No Content
        }
        List<OtherUserDTO> userDTOs = users.stream()
                .map(OtherUserDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.status(200).body(userDTOs);
    }
//      Uncomment to add ability for email search. Would need to update OtherUserDTO
//    @GetMapping("/byEmail")
//    public ResponseEntity<?> searchUsersByEmail(@RequestParam String email) {
//        List<User> users = userRepository.findByEmailContaining(email);
//        if (users.isEmpty()) {
//            return ResponseEntity.status(204).body(null); // No Content
//        }
//        List<OtherUserDTO> userDTOs = users.stream()
//                .map(OtherUserDTO::new)
//                .collect(Collectors.toList());
//        return ResponseEntity.status(200).body(userDTOs);
//    }
}
