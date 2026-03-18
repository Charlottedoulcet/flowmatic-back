package com.flowmatic.flowmatic_back.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import com.flowmatic.flowmatic_back.security.filter.JWTAuthenticationFilter;
import com.flowmatic.flowmatic_back.security.filter.JWTAuthorizationFilter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomAuthenticationManager customAuthenticationManager;
    private final CorsConfigurationSource corsConfigurationSource;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public SecurityConfig(CustomAuthenticationManager customAuthenticationManager,
            CorsConfigurationSource corsConfigurationSource) {
        this.customAuthenticationManager = customAuthenticationManager;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter(customAuthenticationManager,
                jwtSecret,
                jwtExpiration);
        authenticationFilter.setFilterProcessesUrl("/api/auth/login");

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/quotes/**").hasAnyRole("EMPLOYEE", "ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/agency/**").hasRole("ADMIN")
                        .requestMatchers("/api/uploads/**").authenticated()
                        .anyRequest().authenticated())
                .addFilter(authenticationFilter)
                .addFilterAfter(
                        new JWTAuthorizationFilter(customAuthenticationManager, jwtSecret),
                        JWTAuthenticationFilter.class)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> response
                                .sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")));

        return http.build();
    }

}
