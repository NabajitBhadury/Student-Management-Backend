package org.perfecto.springlearning.config;

import org.perfecto.springlearning.utils.jwt_auth.JWTAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JWTAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity.csrf(
                httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorizeRequests ->
                                authorizeRequests.requestMatchers("/api/v1/auth/**").permitAll() // here we are allowing all the requests that start with /api/v1/auth/ to be accessed without authentication because these endpoints will be used for authentication and registration of students and we don't want to require authentication for these endpoints because the students won't have a token yet when they are trying to authenticate or register)
                        .anyRequest().authenticated() // here we are requiring authentication for all the other requests that don't match the above pattern because these endpoints will be protected and can only be accessed by authenticated users who have a valid token
        ).addFilterBefore(
                jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
                ).build();
    }
}
