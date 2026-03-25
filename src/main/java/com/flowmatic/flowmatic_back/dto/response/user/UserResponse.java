package com.flowmatic.flowmatic_back.dto.response.user;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {

  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private List<String> roles;

}
