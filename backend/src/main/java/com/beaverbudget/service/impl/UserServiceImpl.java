package com.beaverbudget.service.impl;

import com.beaverbudget.exceptions.InvalidResourceException;
import com.beaverbudget.exceptions.ResourceNotFoundException;
import com.beaverbudget.model.User;
import com.beaverbudget.persistence.UserPersistenceService;
import com.beaverbudget.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final Integer expirationDays = 90;
    private final UserPersistenceService userPersistenceService;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User createUser(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
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
        return userPersistenceService.findAllUsers();
    }

    @Override
    public User updateUserById(Integer userId, User user) {
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
        if (nonNull(user.getPassword())) {
            LocalDateTime newPasswordExpireDate = LocalDateTime.now().plusMonths(expirationDays);
            existingUser.setPassword(user.getPassword());
            existingUser.setPasswordExpireDate(newPasswordExpireDate);
        }

        return userPersistenceService.saveUser(user);
    }

    @Override
    public void deleteUser(Integer userId) {
        userPersistenceService.deleteUser(userId);
    }
}
