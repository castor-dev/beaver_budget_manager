package com.beaverbudget.controller;

import com.beaverbudget.api.UsersApi;
import com.beaverbudget.mapper.MapperProvider;
import com.beaverbudget.model.User;
import com.beaverbudget.model.UserDTO;
import com.beaverbudget.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UsersApi {

  private final UserService userService;
  private final MapperProvider mapperProvider;

  public UserController(UserService userService, MapperProvider mapperProvider) {
    this.userService = userService;
    this.mapperProvider = mapperProvider;
  }

  @Override
  public ResponseEntity<UserDTO> createUser(UserDTO userDTO) {
    User user = mapperProvider.map(userDTO, User.class);
    user = userService.createUser(user);
    return ResponseEntity.ok(mapperProvider.map(user, UserDTO.class));
  }

  @Override
  public ResponseEntity<Void> deleteUser(Integer userId) {
    userService.deleteUser(userId);
    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<UserDTO> getUserById(Integer userId) {
    User user = userService.findUserById(userId);
    return ResponseEntity.ok(mapperProvider.map(user, UserDTO.class));
  }

  @Override
  public ResponseEntity<List<UserDTO>> listUsers() {
    List<User> allUsers = userService.findAllUsers();
    List<UserDTO> userDTOS =
        allUsers.stream().map(user -> mapperProvider.map(user, UserDTO.class)).toList();
    return ResponseEntity.ok(userDTOS);
  }

  @Override
  public ResponseEntity<UserDTO> updateUser(Integer userId, UserDTO userDTO) {
    User user = mapperProvider.map(userDTO, User.class);
    user = userService.updateUserById(userId, user);
    UserDTO updatedUser = mapperProvider.map(user, UserDTO.class);
    return ResponseEntity.ok(updatedUser);
  }
}
