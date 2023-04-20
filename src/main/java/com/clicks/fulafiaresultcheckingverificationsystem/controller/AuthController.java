package com.clicks.fulafiaresultcheckingverificationsystem.controller;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.LoginRequestDto;
import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.auth.PasswordModel;
import com.clicks.fulafiaresultcheckingverificationsystem.service.auth.AuthService;
import com.clicks.fulafiaresultcheckingverificationsystem.utils.ResponseMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin("https://fulafia-result-frontend-production.up.railway.app/")
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {

        log.debug("Login requested for user {} ", loginRequestDto.email());

        boolean loginResult = authService.login(loginRequestDto);

        log.debug("Login granted for user {} ", loginRequestDto.email());

        return  ResponseEntity.ok(new ResponseMessage("SUCCESS", 0, Map.of("status", loginResult)));

    }


    @PutMapping(path = "/password-reset")
    public ResponseEntity<ResponseMessage> passWordReset(@RequestBody @Valid PasswordModel passwordModel) {
        authService.resetPassword(passwordModel);

        return ResponseEntity.ok(new ResponseMessage("success", 0, Map.of()));
    }
}

