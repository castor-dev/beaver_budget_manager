package com.beaverbudget.service;

import com.beaverbudget.exceptions.FeatureNotImplementedException;
import com.beaverbudget.model.User;
import java.util.List;

public interface UserService {

  /**
   * Creates a new user
   *
   * @param user new user data
   * @return new user
   */
  default User createUser(User user) {
    throw new FeatureNotImplementedException("UserService.createUser not implemented");
  }

  /**
   * Retrieves user with the provided id
   *
   * @param userId user id
   * @return user
   */
  default User findUserById(Integer userId) {
    throw new FeatureNotImplementedException("UserService.findUserById not implemented");
  }

  /**
   * Returns all users
   *
   * @return list of all users
   */
  default List<User> findAllUsers() {
    throw new FeatureNotImplementedException("UserService.findAllUsers not implemented");
  }

  /**
   * Update existing user
   *
   * @param userId existing user id
   * @param user new user data
   * @return updated user
   */
  default User updateUserById(Integer userId, User user) {
    throw new FeatureNotImplementedException("UserService.updateUserById not implemented");
  }

  /**
   * Delete user by id
   *
   * @param userId user id
   */
  default void deleteUser(Integer userId) {
    throw new FeatureNotImplementedException("UserService.deleteUser not implemented");
  }
}
