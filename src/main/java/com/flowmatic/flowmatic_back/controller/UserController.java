package com.flowmatic.flowmatic_back.controller;

import com.flowmatic.flowmatic_back.dto.request.user.UserCreateRequest;
import com.flowmatic.flowmatic_back.dto.request.user.UserUpdateRequest;
import com.flowmatic.flowmatic_back.dto.response.user.UserResponse;
import com.flowmatic.flowmatic_back.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserResponse>> getAllUsers(Authentication auth) {
    return ResponseEntity.ok(userService.getAllUsers(auth.getName()));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserResponse> createUser(
      @Valid @RequestBody UserCreateRequest request,
      Authentication auth) {
    return ResponseEntity.status(201).body(userService.createUser(request, auth.getName()));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserResponse> updateUser(
      @PathVariable Long id,
      @Valid @RequestBody UserUpdateRequest request,
      Authentication auth) {
    return ResponseEntity.ok(userService.updateUser(id, request, auth.getName()));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id, Authentication auth) {
    userService.deleteUser(id, auth.getName());
    return ResponseEntity.noContent().build();
  }

}
