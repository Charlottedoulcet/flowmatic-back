package com.flowmatic.flowmatic_back.dto.request.user;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCreateRequest {

  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @Email
  @NotBlank
  private String email;

  @NotBlank
  @Size(min = 6)
  private String password;

  @NotEmpty
  private Set<String> roles; // ["ADMIN", "EMPLOYEE"] — au moins un rôle requis

}
