package com.clicks.fulafiaresultcheckingverificationsystem.service;

import com.clicks.fulafiaresultcheckingverificationsystem.dtos.requests.ResultGeneralCredentialRequestDto;
import com.clicks.fulafiaresultcheckingverificationsystem.enums.Semester;
import com.clicks.fulafiaresultcheckingverificationsystem.exceptions.ResourceNotFoundException;
import com.clicks.fulafiaresultcheckingverificationsystem.model.ResultGeneralCredential;
import com.clicks.fulafiaresultcheckingverificationsystem.repository.ResultGeneralCredentialRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ResultGeneralCredentialService {

    private final ResultGeneralCredentialRepository resultGeneralCredentialRepository;

    public void init(ResultGeneralCredentialRequestDto resultGeneralCredentialRequestDto) {

        Optional<ResultGeneralCredential> current = resultGeneralCredentialRepository
                .findAll()
                .stream()
                .findFirst();

        if(current.isPresent()) {
            ResultGeneralCredential resultGeneralCredential = current.get();
            resultGeneralCredential.setCurrentSession(resultGeneralCredentialRequestDto.session());
            resultGeneralCredential.setCurrentSemester(Semester.valueOf(resultGeneralCredentialRequestDto.semester()));
        }
        else {

            ResultGeneralCredential resultGeneralCredential = ResultGeneralCredential.builder()
                    .currentSemester(Semester.valueOf(resultGeneralCredentialRequestDto.semester()))
                    .currentSession(resultGeneralCredentialRequestDto.session())
                    .build();

            resultGeneralCredentialRepository.save(resultGeneralCredential);
        }
    }

    public ResultGeneralCredential getResultGeneralCredential() {
        return resultGeneralCredentialRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("Failed to find current result details"));
    }
}
