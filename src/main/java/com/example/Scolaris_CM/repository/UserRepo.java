package com.example.Scolaris_CM.repository;

import com.example.Scolaris_CM.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    Optional<User>findByEmail(String email);

    boolean existsByEmail(String email);
}
