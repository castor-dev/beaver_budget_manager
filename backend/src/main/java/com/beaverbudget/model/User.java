package com.beaverbudget.model;

import java.time.LocalDateTime;
import lombok.*;

/**
 * User model class
 * */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private Integer id;
  private String name;
  private String login;
  private String password;
  private LocalDateTime passwordExpireDate;
}
