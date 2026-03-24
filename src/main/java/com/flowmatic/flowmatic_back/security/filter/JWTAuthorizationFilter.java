package com.flowmatic.flowmatic_back.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  private final String jwtSecret;

  public JWTAuthorizationFilter(AuthenticationManager authenticationManager, String jwtSecret) {
    super(authenticationManager);
    this.jwtSecret = jwtSecret;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    String header = request.getHeader("Authorization");

    if (header == null || !header.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    String token = header.replace("Bearer ", "");

    try {

      DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(jwtSecret))
          .build()
          .verify(token);

      String username = decodedJWT.getSubject();

      String rolesString = decodedJWT.getClaim("roles").asString();
      List<GrantedAuthority> authorities = Arrays.stream(rolesString.split(","))
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());

      Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

      SecurityContextHolder.getContext().setAuthentication(authentication);

      chain.doFilter(request, response);

    } catch (JWTVerificationException e) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide ou expiré");
    }
  }

}
