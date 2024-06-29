package org.epam.service.impl;

import org.epam.repository.UserRepository;
import org.epam.entity.User;
import org.epam.exception.AuthenticationException;
import org.epam.service.UserService;
import org.epam.util.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;
    private AuthenticationService authenticationService;

    UserServiceImpl(
        UserRepository userRepository,
        @Lazy AuthenticationService authenticationService
    ){
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            logger.info("User found with username: {}", username);
        } else {
            logger.warn("User not found with username: {}", username);
        }
        return userOptional;
    }

    @Override
    public boolean changePassword(Long id, String username, String oldPassword, String newPassword) {
        if (authenticationService.authenticate(id, username, oldPassword)) {
            Optional<User> updatedUser = userRepository.changePassword(id, newPassword);
            if (updatedUser.isPresent()) {
                logger.info("Password changed successfully for user with ID: {}", id);
            } else {
                logger.warn("Failed to change password. User with ID: {} not found.", id);
            }
            return true;
        } else {
            logger.error("Authentication failed for user: {}", username);
            throw new AuthenticationException("Authentication failed for user: " + username);
        }
    }
}