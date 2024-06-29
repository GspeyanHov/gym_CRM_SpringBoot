package org.epam.repository;

import org.epam.entity.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUsername(String username);
    Optional<User> changePassword(Long id, String newPassword);
}