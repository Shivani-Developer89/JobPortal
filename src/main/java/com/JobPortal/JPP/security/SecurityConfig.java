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

                        // Public authentication APIs
                        .requestMatchers("/auth/**")
                        .permitAll()

                        // Public uploaded files
                        .requestMatchers("/uploads/**")
                        .permitAll()

                        // Candidate profile creation/update
                        .requestMatchers(
                                HttpMethod.POST,
                                "/candidate-profile/**"
                        )
                        .permitAll()

                        // Candidate profile
                        .requestMatchers(
                                HttpMethod.GET,
                                "/candidate-profile/me"
                        )
                        .hasRole("CANDIDATE")

                        // Recruiter profile access
                        .requestMatchers(
                                HttpMethod.GET,
                                "/candidate-profile/recruiter/**"
                        )
                        .hasRole("RECRUITER")

                        // Public job browsing
                        .requestMatchers(
                                HttpMethod.GET,
                                "/job/all"
                        )
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/job/*"
                        )
                        .permitAll()

                        // Everything else requires authentication
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