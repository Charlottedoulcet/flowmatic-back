package com.flowmatic.flowmatic_back.security;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class JwtTokenService {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private long jwtExpiration;

  public String createToken(String email, Long userId, Long agencyId, Set<String> roles) {
    return JWT.create()
        .withSubject(email)
        .withClaim("userId", userId)
        .withClaim("agencyId", agencyId)
        .withClaim("roles", String.join(",", roles))
        .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiration))
        .sign(Algorithm.HMAC256(jwtSecret));
  }

  public String getSecret() {
    return jwtSecret;
  }
}
