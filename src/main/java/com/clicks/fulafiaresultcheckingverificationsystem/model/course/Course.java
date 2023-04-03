package com.clicks.fulafiaresultcheckingverificationsystem.model.course;

import com.clicks.fulafiaresultcheckingverificationsystem.enums.Level;
import com.clicks.fulafiaresultcheckingverificationsystem.enums.Semester;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @Column(unique = true)
    private String title;

    @Column(unique = true)
    private String code;
    private Integer unit;

    @Enumerated(STRING)
    private Semester semester;


}
