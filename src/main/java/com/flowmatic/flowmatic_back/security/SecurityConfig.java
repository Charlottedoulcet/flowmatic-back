package com.flowmatic.flowmatic_back.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import com.flowmatic.flowmatic_back.security.filter.JWTAuthenticationFilter;
import com.flowmatic.flowmatic_back.security.filter.JWTAuthorizationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final CustomAuthenticationManager customAuthenticationManager;
  private final CorsConfigurationSource corsConfigurationSource;
  private final JwtTokenService jwtTokenService;

  @Value("${jwt.secret}")
  private String jwtSecret;

  public SecurityConfig(CustomAuthenticationManager customAuthenticationManager,
      CorsConfigurationSource corsConfigurationSource,
      JwtTokenService jwtTokenService) {
    this.customAuthenticationManager = customAuthenticationManager;
    this.corsConfigurationSource = corsConfigurationSource;
    this.jwtTokenService = jwtTokenService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter(
        customAuthenticationManager, jwtTokenService);
    authenticationFilter.setFilterProcessesUrl("/api/auth/login");

    http
        .csrf(csrf -> csrf.disable())
        .cors(cors -> cors.configurationSource(corsConfigurationSource))
        .addFilterBefore(new RegistrationRateLimitFilter(),
            JWTAuthenticationFilter.class)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
            .requestMatchers("/error").permitAll()
            .requestMatchers("/api/quotes/**").hasAnyRole("EMPLOYEE")
            .requestMatchers("/api/users/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/agency").authenticated()
            .requestMatchers("/api/agency/**").hasRole("ADMIN")
            .requestMatchers("/api/uploads/**").authenticated()
            .anyRequest().authenticated())
        .addFilter(authenticationFilter)
        .addFilterAfter(
            new JWTAuthorizationFilter(customAuthenticationManager, jwtSecret),
            JWTAuthenticationFilter.class)
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((request, response, authException) ->
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")));

    return http.build();
  }
}
