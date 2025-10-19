package com.beaverbudget.service.impl;

import com.beaverbudget.exceptions.InvalidResourceException;
import com.beaverbudget.exceptions.ResourceNotFoundException;
import com.beaverbudget.model.User;
import com.beaverbudget.persistence.UserPersistenceService;
import com.beaverbudget.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final Integer expirationDays = 90;
    private final UserPersistenceService userPersistenceService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User createUser(User user) {
        log.info("creating user {}", user);
        String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
        user.setPasswordHash(hashedPassword);
        LocalDateTime passwordExpirationDate = LocalDateTime.now().plusDays(expirationDays);
        user.setPasswordExpireDate(passwordExpirationDate);
        return userPersistenceService.createUser(user);
    }

    @Override
    public User findUserById(Integer userId) {
        return userPersistenceService.findUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
    }

    @Override
    public List<User> findAllUsers() {
        log.info("find all users");
        return userPersistenceService.findAllUsers();
    }

    @Override
    public User updateUserById(Integer userId, User user) {
        log.info("updating user {} with {}", userId, user);
        User existingUser = userPersistenceService.findUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        if (nonNull(user.getName())) {
            existingUser.setName(user.getName());
        }
        if (nonNull(user.getEmail())) {
            String email = user.getEmail();
            userPersistenceService.findUserByEmail(email).ifPresent(u -> {
                throw new InvalidResourceException("User with email " + email + "already exists");
            });
            existingUser.setEmail(email);
        }
        if (nonNull(user.getPasswordHash())) {
            String hashedPassword = passwordEncoder.encode(user.getPasswordHash());
            existingUser.setPasswordHash(hashedPassword);
            LocalDateTime newPasswordExpireDate = LocalDateTime.now().plusMonths(expirationDays);
            existingUser.setPasswordHash(user.getPasswordHash());
            existingUser.setPasswordExpireDate(newPasswordExpireDate);
        }

        return userPersistenceService.saveUser(user);
    }

    @Override
    public void deleteUser(Integer userId) {
        log.info("deleting user {}", userId);
        userPersistenceService.deleteUser(userId);
    }
}
