package com.project.localbrew.config;

import com.project.localbrew.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disabilita CSRF
        http.csrf(csrf -> csrf.disable())
                // JWT = stateless
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Permessi endpoint
                .authorizeHttpRequests(auth -> auth
                        // Endpoint pubblici
                        .requestMatchers("/api/auth/**", "/api/map/**", "/api/breweries/public/**").permitAll()
                        // Solo ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // Solo OWNER
                        .requestMatchers("/api/owner/**").hasRole("OWNER")
                        // Qualsiasi altra request richiede login
                        .anyRequest().authenticated())
                // JWT Filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Basic config
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}