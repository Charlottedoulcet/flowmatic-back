package com.flowmatic.flowmatic_back.service;

import com.flowmatic.flowmatic_back.dto.request.RegisterRequest;
import com.flowmatic.flowmatic_back.dto.response.AuthResponse;

public interface AuthService {

  public AuthResponse register(RegisterRequest request);

}
