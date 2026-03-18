package com.flowmatic.flowmatic_back.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Component;

import com.flowmatic.flowmatic_back.service.UserService;

@Component
public class CustomAuthenticationManager implements AuthenticationManager {

  private final UserService userService;
  private final BCryptPasswordEncoder passwordEncoder;

  public CustomAuthenticationManager(UserService userservice, BCryptPasswordEncoder passwordEncoder) {
    this.userService = userservice;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UserDetail userDetail = (UserDetail) userService.loadUserByUsername(authentication.getName());

    if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetail.getPassword())) {
      throw new BadCredentialsException("Mot de passe incorrect");
    }

    return new UsernamePasswordAuthenticationToken(
        userDetail,
        userDetail.getPassword(),
        userDetail.getAuthorities());
  }
}
