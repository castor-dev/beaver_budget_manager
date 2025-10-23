package com.beaverbudget.persistence.repository;

import com.beaverbudget.persistence.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    List<UserEntity> findByEmail(String email);

}
