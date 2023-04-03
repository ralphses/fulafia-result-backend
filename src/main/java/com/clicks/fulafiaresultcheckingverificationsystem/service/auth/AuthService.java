package com.clicks.fulafiaresultcheckingverificationsystem.service.auth;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.LoginRequestDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.auth.PasswordModel;
import com.clicks.fulafiaresultcheckingverificationsystem.exceptions.InvalidRequestParamException;
import com.clicks.fulafiaresultcheckingverificationsystem.exceptions.UnauthorizedUserException;
import com.clicks.fulafiaresultcheckingverificationsystem.model.User;
import com.clicks.fulafiaresultcheckingverificationsystem.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public void login(LoginRequestDto loginRequestDto) {
        User user = userService.findUserByEmail(loginRequestDto.email());
        if(!passwordEncoder.matches(loginRequestDto.password(), user.getPassword())) {
            throw new UnauthorizedUserException("Invalid Login Credentials");
        }
    }

    public void resetPassword(PasswordModel passwordModel) {

        if (Objects.equals(passwordModel.newPassword(), passwordModel.confirmPassword())) {
            User user = userService.findUserByEmail(passwordModel.email());
            user.setPassword(passwordEncoder.encode(passwordModel.newPassword()));
        }
        else throw new InvalidRequestParamException("New Password must match confirm password");

    }
}
