package com.beaverbudget.persistence.repository.entity;

import com.beaverbudget.model.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "account")
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type; // e.g. INCOME, EXPENSE, TRANSFER

    @Column(nullable = false)
    private LocalDateTime date;
    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private AccountEntity account;

    @Column(name = "is_planned", nullable = false)
    private boolean planned = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_transaction_id")
    private TransactionEntity parentTransaction;

    @OneToMany(mappedBy = "parentTransaction")
    private Set<TransactionEntity> childTransactions;

}
