package com.beaverbudget.persistence.impl;

import com.beaverbudget.exceptions.InvalidResourceException;
import com.beaverbudget.mapper.MapperProvider;
import com.beaverbudget.model.User;
import com.beaverbudget.persistence.UserPersistenceService;
import com.beaverbudget.persistence.repository.UserRepository;
import com.beaverbudget.persistence.repository.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
public class UserPersistenceServiceImpl implements UserPersistenceService {

    private final MapperProvider mapperProvider;
    private final UserRepository userRepository;

    public UserPersistenceServiceImpl(MapperProvider mapperProvider, UserRepository userRepository) {
        this.mapperProvider = mapperProvider;
        this.userRepository = userRepository;
    }


    @Override
    public User saveUser(User user) {
        List<UserEntity> usersWithProvidedEmail = userRepository.findByEmail(user.getEmail());
        if (!CollectionUtils.isEmpty(usersWithProvidedEmail)) {
            throw new InvalidResourceException("user email not available");
        }
        UserEntity userEntity = mapperProvider.map(user, UserEntity.class);
        UserEntity savedUser = userRepository.save(userEntity);
        return mapperProvider.map(savedUser, User.class);
    }

    @Override
    public Optional<User> findUserById(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        return Optional.ofNullable(mapperProvider.map(userEntity, User.class));
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> mapperProvider.map(user, User.class))
                .toList();
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
}

