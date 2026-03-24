package com.flowmatic.flowmatic_back.security.filter;

import java.io.IOException;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowmatic.flowmatic_back.dto.request.auth.LoginRequest;
import com.flowmatic.flowmatic_back.dto.response.auth.AuthResponse;
import com.flowmatic.flowmatic_back.security.CustomAuthenticationManager;
import com.flowmatic.flowmatic_back.security.UserDetail;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final CustomAuthenticationManager authentificationManager;
  private final String jwtSecret;
  private final long jwtExpiration;

  public JWTAuthenticationFilter(CustomAuthenticationManager authenticationManager, String jwtSecret,
      long jwtExpiration) {
    super(authenticationManager);
    this.authentificationManager = authenticationManager;
    this.jwtSecret = jwtSecret;
    this.jwtExpiration = jwtExpiration;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {
    try {
      LoginRequest loginRequest = new ObjectMapper()
          .readValue(request.getInputStream(), LoginRequest.class);

      Authentication authentication = new UsernamePasswordAuthenticationToken(
          loginRequest.getEmail(),
          loginRequest.getPassword());

      return authentificationManager.authenticate(authentication);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException failed) throws IOException {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, failed.getMessage());
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authentication) throws IOException {

    UserDetail userDetail = (UserDetail) authentication.getPrincipal();

    Set<String> roles = userDetail.getAuthorities().stream()
        .map(auth -> auth.getAuthority())
        .collect(Collectors.toSet());

    String token = JWT.create()
        .withSubject(userDetail.getUsername())
        .withClaim("userId", userDetail.getUser().getId())
        .withClaim("agencyId", userDetail.getUser().getAgency().getId())
        .withClaim("roles", String.join(",", roles))
        .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiration))
        .sign(Algorithm.HMAC256(jwtSecret));

    AuthResponse authResponse = new AuthResponse(
        "Bearer " + token,
        userDetail.getUser().getId(),
        userDetail.getUser().getAgency().getId(),
        userDetail.getUser().getFirstName(),
        userDetail.getUser().getLastName(),
        roles);

    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    new ObjectMapper().writeValue(response.getWriter(), authResponse);
  }
}
