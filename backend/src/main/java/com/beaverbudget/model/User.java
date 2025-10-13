package com.beaverbudget.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * User model class
 */
public class User {
    private Integer id;
    private String name;
    private String login;
    private String password;
    private LocalDateTime passwordExpireDate;
}
