package com.flowmatic.flowmatic_back.service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.flowmatic.flowmatic_back.dto.request.RegisterRequest;
import com.flowmatic.flowmatic_back.dto.response.AuthResponse;
import com.flowmatic.flowmatic_back.entity.Agency;
import com.flowmatic.flowmatic_back.entity.User;
import com.flowmatic.flowmatic_back.entity.enums.Role;
import com.flowmatic.flowmatic_back.exception.DuplicateResourceException;
import com.flowmatic.flowmatic_back.repository.AgencyRepository;
import com.flowmatic.flowmatic_back.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

  private final AgencyRepository agencyRepository;
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")
  private long jwtExpiration;

  public AuthServiceImpl(UserRepository userRepository, AgencyRepository agencyRepository,
      BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.agencyRepository = agencyRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public AuthResponse register(RegisterRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateResourceException("Un compte existe déjà avec cet email: " + request.getEmail());
    }

    Agency agency = new Agency();
    agency.setName(request.getAgencyName());
    agency = agencyRepository.save(agency);

    User user = new User();
    user.setAgency(agency);
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRoles(new HashSet<>(Set.of(Role.ADMIN, Role.EMPLOYEE)));
    user = userRepository.save(user);

    Set<String> roles = user.getRoles().stream()
        .map(role -> "ROLE_" + role.name())
        .collect(Collectors.toSet());

    String token = JWT.create()
        .withSubject(user.getEmail())
        .withClaim("userId", user.getId())
        .withClaim("agencyId", agency.getId())
        .withClaim("roles", String.join(",", roles))
        .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpiration))

        .sign(Algorithm.HMAC256(jwtSecret));

    return new AuthResponse("Bearer " + token, user.getId(), agency.getId(), user.getFirstName(), roles);
  }
}
