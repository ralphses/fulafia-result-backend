package com.clicks.fulafiaresultcheckingverificationsystem.model;

import com.clicks.fulafiaresultcheckingverificationsystem.enums.Semester;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.AUTO;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultGeneralCredential {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Enumerated(STRING)
    private Semester currentSemester;

    private String currentSession;
}
