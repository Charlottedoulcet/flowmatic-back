package com.flowmatic.flowmatic_back.service;

import com.flowmatic.flowmatic_back.dto.request.auth.RegisterRequest;
import com.flowmatic.flowmatic_back.dto.response.auth.AuthResponse;

public interface AuthService {

  public AuthResponse register(RegisterRequest request);

}
