package com.flowmatic.flowmatic_back.service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowmatic.flowmatic_back.dto.request.auth.RegisterRequest;
import com.flowmatic.flowmatic_back.dto.response.auth.AuthResponse;
import com.flowmatic.flowmatic_back.entity.Agency;
import com.flowmatic.flowmatic_back.entity.User;
import com.flowmatic.flowmatic_back.entity.enums.Role;
import com.flowmatic.flowmatic_back.exception.DuplicateResourceException;
import com.flowmatic.flowmatic_back.repository.AgencyRepository;
import com.flowmatic.flowmatic_back.repository.UserRepository;
import com.flowmatic.flowmatic_back.security.JwtTokenService;

@Service
public class AuthServiceImpl implements AuthService {

  private final AgencyRepository agencyRepository;
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;
  private final JwtTokenService jwtTokenService;

  public AuthServiceImpl(UserRepository userRepository, AgencyRepository agencyRepository,
      BCryptPasswordEncoder passwordEncoder, JwtTokenService jwtTokenService) {
    this.userRepository = userRepository;
    this.agencyRepository = agencyRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenService = jwtTokenService;
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

    String token = jwtTokenService.createToken(user.getEmail(), user.getId(), agency.getId(), roles);

    return new AuthResponse("Bearer " + token, user.getId(), agency.getId(),
        user.getFirstName(), user.getLastName(), roles);
  }
}
