package com.mykola.crm.repository;

import java.util.Optional;

import com.mykola.crm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsernameOrEmail(String username, String email);
}