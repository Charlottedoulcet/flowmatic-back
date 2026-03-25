package com.flowmatic.flowmatic_back.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

  @NotNull
  private String role;

}
