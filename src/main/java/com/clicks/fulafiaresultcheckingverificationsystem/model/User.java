package com.clicks.fulafiaresultcheckingverificationsystem.model;

import com.clicks.fulafiaresultcheckingverificationsystem.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.AUTO;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;
    private  String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private Boolean isLocked;

    @Column(unique = true)
    private String staffId;

}
