package com.clicks.fulafiaresultcheckingverificationsystem.controller;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.ResultGeneralCredentialRequestDto;
import com.clicks.fulafiaresultcheckingverificationsystem.service.ResultGeneralCredentialService;
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
@RequestMapping(value = "/api/v1/initialize")
public class ResultGeneralCredentialController {

    private final ResultGeneralCredentialService resultGeneralCredentialService;

    @PostMapping("/")
    public ResponseEntity<ResponseMessage> init(@RequestBody @Valid ResultGeneralCredentialRequestDto resultGeneralCredentialRequestDto) {

        resultGeneralCredentialService.init(resultGeneralCredentialRequestDto);

        return ResponseEntity.ok(new ResponseMessage("SUCCESS", 0, Map.of()));
    }
}
