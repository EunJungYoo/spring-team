package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUserId(String userId);

    Optional<User> findUserByUserId(String userId);

    boolean existsByUserId(String userId);

    void deleteByUserId(String name);

    ShowMypage findByUsernameAndUserId(String username, String userId);
}