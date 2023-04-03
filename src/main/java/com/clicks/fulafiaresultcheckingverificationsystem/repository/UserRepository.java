package com.clicks.fulafiaresultcheckingverificationsystem.repository;

import com.clicks.fulafiaresultcheckingverificationsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT user FROM User user WHERE user.email = ?1")
    Optional<User> findByEmail(String email);
}
