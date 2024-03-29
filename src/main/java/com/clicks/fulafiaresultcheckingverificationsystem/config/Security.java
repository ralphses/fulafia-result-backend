package com.clicks.fulafiaresultcheckingverificationsystem.config;

import com.clicks.fulafiaresultcheckingverificationsystem.enums.CourseType;
import com.clicks.fulafiaresultcheckingverificationsystem.enums.Semester;
import com.clicks.fulafiaresultcheckingverificationsystem.model.ResultGeneralCredential;
import com.clicks.fulafiaresultcheckingverificationsystem.model.User;
import com.clicks.fulafiaresultcheckingverificationsystem.model.course.Course;
import com.clicks.fulafiaresultcheckingverificationsystem.model.department.Department;
import com.clicks.fulafiaresultcheckingverificationsystem.model.department.DepartmentCourse;
import com.clicks.fulafiaresultcheckingverificationsystem.repository.*;
import com.clicks.fulafiaresultcheckingverificationsystem.service.auth.UserDetailsService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.clicks.fulafiaresultcheckingverificationsystem.enums.UserRole.ADMIN;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class Security {

    @Value("${admin.sender}")
    private String adminEmail;

    @Value("${admin.name}")
    private String adminName;

    @Value("${admin.staffId}")
    private String staffId;

    @Value("${admin.frontend.url}")
    private String frontEndUrl;


    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final UserDetailsService userDetailsService;
    private final DepartmentRepository departmentRepository;
    private final DepartmentCourseRepository departmentCourseRepository;
    private final ResultGeneralCredentialRepository resultGeneralCredentialRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity

                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer -> corsConfigurationSource())

                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())

                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))

                .userDetailsService(userDetailsService)
                .httpBasic()
                .and()

                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of(frontEndUrl));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/api/**", corsConfiguration);

        return urlBasedCorsConfigurationSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    @Profile({"dev", "staging"})
    CommandLineRunner commandLineRunner() {

        return args -> {
            User user = User.builder()
                    .email(adminEmail)
                    .name(adminName)
                    .userRole(ADMIN)
                    .staffId(staffId)
                    .isLocked(false)
                    .password(passwordEncoder().encode("password"))
                    .build();

            userRepository.save(user);

            generateCourses();

            List<DepartmentCourse> departmentCourses  = generateDepartmentCourses();

            departmentRepository.save(
                    Department.builder()
                            .courses(departmentCourses)
                            .code("CSC")
                            .name("Computer Science")
                            .build()
            );

            resultGeneralCredentialRepository.save(ResultGeneralCredential.builder()
                            .currentSession("2021/2022")
                            .currentSemester(Semester.FIRST)
                    .build());

        };
    }


    private List<DepartmentCourse> generateDepartmentCourses() {
        return departmentCourseRepository.saveAllAndFlush(

                courseRepository.findAll()
                        .stream()
                        .map(course -> DepartmentCourse.builder()
                                .type(CourseType.CORE)
                                .level(1)
                                .course(course)
                                .build())
                        .toList()
        );
    }

    private void generateCourses() {
        courseRepository.saveAll(
                List.of(
                        Course.builder()
                                .code("CSC111")
                                .semester(Semester.FIRST)
                                .title("Introduction to Computer Science")
                                .unit(3)
                                .build(),

                        Course.builder()
                                .code("CSC121")
                                .semester(Semester.SECOND)
                                .title("Introduction to Problem solving and Algorithm")
                                .unit(3)
                                .build(),

                        Course.builder()
                                .code("MTH111")
                                .semester(Semester.FIRST)
                                .title("General Mathematics")
                                .unit(3)
                                .build(),

                        Course.builder()
                                .code("MTH122")
                                .semester(Semester.SECOND)
                                .title("Vector, Geometry and Dynamics")
                                .unit(3)
                                .build(),

                        Course.builder()
                                .code("GST111")
                                .semester(Semester.FIRST)
                                .title("Use of English I")
                                .unit(2)
                                .build(),

                        Course.builder()
                                .code("PHY111")
                                .semester(Semester.FIRST)
                                .title("Introduction to Physics")
                                .unit(3)
                                .build(),

                        Course.builder()
                                .code("BLY111")
                                .semester(Semester.FIRST)
                                .title("Introduction to Biology")
                                .unit(3)
                                .build()
                        )
        );
    }
}
