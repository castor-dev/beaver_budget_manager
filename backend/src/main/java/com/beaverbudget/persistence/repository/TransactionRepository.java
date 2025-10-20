package com.beaverbudget.persistence.repository;

import com.beaverbudget.persistence.repository.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {}
