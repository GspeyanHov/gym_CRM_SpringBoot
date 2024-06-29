package org.epam.controller;

import java.util.Optional;
import org.epam.entity.User;
import org.epam.service.UserService;
import org.epam.util.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class LoginController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    LoginController(AuthenticationService authenticationService, UserService userService){
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam("username") String username,
                                        @RequestParam("password") String password) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean isAuthenticated = authenticationService.authenticate(user.getId(), username, password);
            if (isAuthenticated) {
                return ResponseEntity.ok("200 OK");
            } else {
                return ResponseEntity.ok("Invalid credentials");
            }
        } else {
            return ResponseEntity.ok("User not found");
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam("username") String username,
                                                 @RequestParam("oldPassword") String oldPassword,
                                                 @RequestParam("newPassword") String newPassword) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean passwordChanged = userService.changePassword(user.getId(), username, oldPassword, newPassword);

            if (passwordChanged) {
                return ResponseEntity.ok("Password changed successfully");
            } else {
                return ResponseEntity.ok("Password change failed. Verify your credentials.");
            }
        } else {
            return ResponseEntity.ok("User not found. Verify your credentials.");
        }
    }
}