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
    private String description;
    private BigDecimal amount;
    private Integer accountId;
    private Boolean planned;
    private Transaction parentTransaction;
}
