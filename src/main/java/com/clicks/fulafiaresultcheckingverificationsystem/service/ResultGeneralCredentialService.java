package com.clicks.fulafiaresultcheckingverificationsystem.service;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.ResultGeneralCredentialRequestDto;
import com.clicks.fulafiaresultcheckingverificationsystem.enums.Semester;
import com.clicks.fulafiaresultcheckingverificationsystem.exceptions.ResourceNotFoundException;
import com.clicks.fulafiaresultcheckingverificationsystem.model.ResultGeneralCredential;
import com.clicks.fulafiaresultcheckingverificationsystem.repository.ResultGeneralCredentialRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ResultGeneralCredentialService {

    private final ResultGeneralCredentialRepository resultGeneralCredentialRepository;

    public void init(ResultGeneralCredentialRequestDto resultGeneralCredentialRequestDto) {

        ResultGeneralCredential resultGeneralCredential = ResultGeneralCredential.builder()
                .currentSemester(Semester.valueOf(resultGeneralCredentialRequestDto.semester()))
                .currentSession(resultGeneralCredentialRequestDto.session())
                .build();

        resultGeneralCredentialRepository.save(resultGeneralCredential);
    }

    public ResultGeneralCredential getResultGeneralCredential() {
        return resultGeneralCredentialRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Failed to find current result details"));
    }
}
