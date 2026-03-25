package com.flowmatic.flowmatic_back.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.flowmatic.flowmatic_back.dto.request.user.UserCreateRequest;
import com.flowmatic.flowmatic_back.dto.request.user.UserUpdateRequest;
import com.flowmatic.flowmatic_back.dto.response.user.UserResponse;

public interface UserService extends UserDetailsService {

    public List<UserResponse> getAllUsers(String userEmail);

    public UserResponse createUser(UserCreateRequest request, String userEmail);

    public UserResponse updateUser(Long id, UserUpdateRequest request, String userEmail);

    public void deleteUser(Long id, String userEmail);
}
