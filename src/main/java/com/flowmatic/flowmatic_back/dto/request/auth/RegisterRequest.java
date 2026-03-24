package com.flowmatic.flowmatic_back.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

  @NotBlank
  private String agencyName;

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 6)
  private String password;
}
