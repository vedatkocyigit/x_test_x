package com.not_projesi.config;

import com.not_projesi.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth

            // 🔓 ACTUATOR
            .requestMatchers("/actuator/health").permitAll()

            // 🔓 FRONTEND (SPA ROUTES)
            .requestMatchers(
                "/",
                "/index.html",
                "/login",
                "/register",
                "/home",
                "/profile-update",
                "/ders-notu-ekle",
                "/ders-ekle",
                "/favorilerim",
                "/notlarim",
                "/assets/**",
                "/favicon.ico"
            ).permitAll()

            // 🔓 BACKEND AUTH & API
            .requestMatchers(
                "/auth/**",
                "/authenticate",
                "/refreshtoken",
                "/rest/guncelle/profil-bilgi/{username}",
                "/rest/**",
                "/rest/sepet/**",
                "/ai/**",
                "/ders-notlari-onizleme/**",
                "/ders-notlari/**",
                "/swagger-ui/**",
                "/notes/upload",
                "/v3/api-docs/**",
                "/swagger.html"
            ).permitAll()

            // 🔐 DİĞERLERİ
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
}
