package com.study.clouddatastorage.repository;

import com.study.clouddatastorage.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Boolean existsUserByEmail(String email);
    Boolean existsUserByUsername(String username);
}
