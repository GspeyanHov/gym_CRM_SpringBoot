package org.epam.util;

import org.epam.entity.User;
import org.epam.exception.AuthenticationException;
import org.epam.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserService userService;

    AuthenticationService(@Lazy UserService userService){
        this.userService = userService;
    }

    public boolean authenticate(Long id, String username, String password) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getId().equals(id) && user.getPassword().equals(password)) {
                return true;
            }
        }
        throw new AuthenticationException("Authentication failed for user: " + username);
    }
}