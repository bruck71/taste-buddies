package org.launchcode.TasteBuddiesServer.services;

import org.launchcode.TasteBuddiesServer.config.JwtUtil;
import org.launchcode.TasteBuddiesServer.data.UserRepository;
import org.launchcode.TasteBuddiesServer.exception.UserNotFoundException;
import org.launchcode.TasteBuddiesServer.models.User;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public User getUserFromRequest(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
        String email = jwtUtil.extractUsername(token);
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public List<User> searchUsersByDisplayName(String query) {
        return userRepository.findByDisplayNameContaining(query);
    }

    public List<User> searchUsersByEmail(String query) {
        return userRepository.findByEmailContaining(query);
    }

}
