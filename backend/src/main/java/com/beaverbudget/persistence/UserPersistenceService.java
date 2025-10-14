package com.beaverbudget.persistence;

import com.beaverbudget.exceptions.FeatureNotImplementedException;
import com.beaverbudget.model.Account;
import com.beaverbudget.model.User;

import java.util.List;
import java.util.Optional;

public interface UserPersistenceService {

    default User createUser(User user ){
        throw new FeatureNotImplementedException("UserPersistenceService.createUser not implemented");
    }

    default Optional<User> findUserById(Integer userId){
        throw new FeatureNotImplementedException("UserPersistenceService.findUserById not implemented");
    }

    default Optional<User> findUserByEmail(String userEmail){
        throw new FeatureNotImplementedException("UserPersistenceService.findUserByEmail not implemented");
    }

    default List<User> findAllUsers(){
        throw new FeatureNotImplementedException("UserPersistenceService.findAllUsers not implemented");
    }

    default User saveUser(User user){
        throw new FeatureNotImplementedException("UserPersistenceService.saveUser not implemented");
    }

    default void deleteUser(Integer userId){
        throw new FeatureNotImplementedException("UserPersistenceService.deleteUser not implemented");
    }
}
