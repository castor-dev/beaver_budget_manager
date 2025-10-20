package com.beaverbudget.persistence.repository;

import com.beaverbudget.persistence.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {}
