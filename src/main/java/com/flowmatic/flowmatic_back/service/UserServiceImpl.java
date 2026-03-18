package com.flowmatic.flowmatic_back.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.flowmatic.flowmatic_back.repository.UserRepository;
import com.flowmatic.flowmatic_back.security.UserDetail;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email)
        .map(user -> new UserDetail(user))
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
  }
}
