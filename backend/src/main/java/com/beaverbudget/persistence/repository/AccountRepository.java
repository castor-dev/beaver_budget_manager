package com.beaverbudget.persistence.repository;

import com.beaverbudget.persistence.repository.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {}
