package com.flowmatic.flowmatic_back.dto.response.auth;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

  private String token;
  private Long userId;
  private Long agencyId;
  private String firstName;
  private String lastName;
  private Set<String> roles;

}
