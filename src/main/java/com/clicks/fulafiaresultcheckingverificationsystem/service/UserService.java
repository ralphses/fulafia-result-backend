package com.clicks.fulafiaresultcheckingverificationsystem.service;

import com.clicks.fulafiaresultcheckingverificationsystem.model.User;
import com.clicks.fulafiaresultcheckingverificationsystem.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for " + email));
    }
}
