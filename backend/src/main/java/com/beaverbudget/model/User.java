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
  private String email;
  private String passwordHash;
  private LocalDateTime passwordExpireDate;
}
