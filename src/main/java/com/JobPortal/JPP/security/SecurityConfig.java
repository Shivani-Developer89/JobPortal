package com.JobPortal.JPP.security;

import com.JobPortal.JPP.security.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // PUBLIC AUTH
                        // =========================

                        .requestMatchers("/auth/**")
                        .permitAll()


                        // =========================
                        // PUBLIC UPLOADS
                        // =========================

                        .requestMatchers("/uploads/**")
                        .permitAll()


                        // =========================
                        // CANDIDATE PROFILE
                        // =========================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/candidate-profile/**"
                        )
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/candidate-profile/me"
                        )
                        .hasRole("CANDIDATE")


                        // =========================
                        // RECRUITER PROFILE
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/candidate-profile/recruiter/**"
                        )
                        .hasRole("RECRUITER")
                                // =========================
// RECRUITER VIEW CANDIDATE PROFILE IMAGE
// =========================

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/candidate-profile/candidate/*/profile-image"
                                )
                                .hasRole("RECRUITER")


                        // =========================
                        // PUBLIC JOB BROWSING
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/job/all"
                        )
                        .permitAll()


                        // =========================
                        // RECRUITER JOBS
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/job/my"
                        )
                        .hasRole("RECRUITER")


                        // =========================
                        // CANDIDATE SAVED JOBS
                        // IMPORTANT:
                        // This must come BEFORE /job/*
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/job/saved"
                        )
                        .hasRole("CANDIDATE")


                        // =========================
                        // CANDIDATE SAVE JOB
                        // =========================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/job/*/save"
                        )
                        .hasRole("CANDIDATE")


                        // =========================
                        // CANDIDATE UNSAVE JOB
                        // =========================

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/job/*/unsave"
                        )
                        .hasRole("CANDIDATE")


                        // =========================
                        // RECRUITER CREATE JOB
                        // =========================

                        .requestMatchers(
                                HttpMethod.POST,
                                "/job"
                        )
                        .hasRole("RECRUITER")


                        // =========================
                        // RECRUITER UPDATE / CLOSE /
                        // REOPEN JOB
                        // =========================

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/job/*"
                        )
                        .hasRole("RECRUITER")


                        // =========================
                        // RECRUITER DELETE JOB
                        // =========================

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/job/*"
                        )
                        .hasRole("RECRUITER")


                        // =========================
                        // PUBLIC SINGLE JOB / SEARCH
                        // =========================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/job/*"
                        )
                        .permitAll()


                        // =========================
                        // EVERYTHING ELSE
                        // =========================

                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}