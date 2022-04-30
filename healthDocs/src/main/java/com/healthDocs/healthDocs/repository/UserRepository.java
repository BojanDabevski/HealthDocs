package com.healthDocs.healthDocs.repository;

import com.healthDocs.healthDocs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String s);
    Optional<User> findById(Long Id);
         Optional<User>findByUsernameAndPassword(String username, String password);
}