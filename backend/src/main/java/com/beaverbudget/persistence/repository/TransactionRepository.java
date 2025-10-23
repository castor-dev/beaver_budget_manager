package com.beaverbudget.persistence.repository;

import com.beaverbudget.persistence.repository.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {

    @Query("""
            SELECT tr
            FROM TransactionEntity tr
            WHERE tr.account.id = :accountId
                AND (:from is NULL OR tr.date >= :from)
                AND (:to is NULL OR tr.date <= :to)
            ORDER BY tr.date DESC
            """)
    List<TransactionEntity> findAccountTransactions(Integer accountId, LocalDateTime from, LocalDateTime to);

    @Query("""
            SELECT tr
            FROM TransactionEntity tr
            WHERE (:from is NULL OR tr.date >= :from)
                AND (:to is NULL OR tr.date <= :to)
            ORDER BY tr.date DESC
            """)
    List<TransactionEntity> findTransactions(LocalDateTime from, LocalDateTime to);
}