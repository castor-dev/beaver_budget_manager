package com.beaverbudget.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    private Integer id;
    private String name;
    private User owner;
    private BigDecimal balance;
    private String currency;
}
