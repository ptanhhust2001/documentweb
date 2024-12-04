package com.hust.documentweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hust.documentweb.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
