package com.beaverbudget.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    private Integer id;
    private BigDecimal amount;
    private String type;
    private String description;
    private Integer accountId;
    private Boolean planned;
}
