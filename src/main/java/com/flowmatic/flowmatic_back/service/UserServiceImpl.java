package com.flowmatic.flowmatic_back.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowmatic.flowmatic_back.dto.request.user.UserCreateRequest;
import com.flowmatic.flowmatic_back.dto.request.user.UserUpdateRequest;
import com.flowmatic.flowmatic_back.dto.response.user.UserResponse;
import com.flowmatic.flowmatic_back.entity.User;
import com.flowmatic.flowmatic_back.entity.enums.Role;
import com.flowmatic.flowmatic_back.exception.BadRequestException;
import com.flowmatic.flowmatic_back.exception.DuplicateResourceException;
import com.flowmatic.flowmatic_back.exception.ResourceNotFoundException;
import com.flowmatic.flowmatic_back.mapper.UserMapper;
import com.flowmatic.flowmatic_back.repository.UserRepository;
import com.flowmatic.flowmatic_back.security.UserDetail;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public UserServiceImpl(UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      UserMapper userMapper) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.userMapper = userMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository.findByEmail(email)
        .map(UserDetail::new)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponse> getAllUsers(String userEmail) {
    User currentUser = findUserByEmail(userEmail);
    return userRepository.findByAgencyId(currentUser.getAgency().getId())
        .stream()
        .map(userMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional
  public UserResponse createUser(UserCreateRequest request, String userEmail) {
    User currentUser = findUserByEmail(userEmail);

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateResourceException("Email déjà utilisé : " + request.getEmail());
    }

    Role role = parseRole(request.getRole());

    User user = new User();
    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setAgency(currentUser.getAgency());
    user.getRoles().add(role);

    return userMapper.toResponse(userRepository.save(user));
  }

  @Override
  @Transactional
  public UserResponse updateUser(Long id, UserUpdateRequest request, String userEmail) {
    User currentUser = findUserByEmail(userEmail);
    User user = userRepository.findByIdAndAgencyId(id, currentUser.getAgency().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé : " + id));

    user.setFirstName(request.getFirstName());
    user.setLastName(request.getLastName());
    user.setEmail(request.getEmail());

    if (request.getPassword() != null && !request.getPassword().isBlank()) {
      user.setPassword(passwordEncoder.encode(request.getPassword()));
    }

    Role role = parseRole(request.getRole());
    user.getRoles().clear();
    user.getRoles().add(role);

    return userMapper.toResponse(userRepository.save(user));
  }

  @Override
  @Transactional
  public void deleteUser(Long id, String userEmail) {
    User currentUser = findUserByEmail(userEmail);
    User user = userRepository.findByIdAndAgencyId(id, currentUser.getAgency().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé : " + id));

    if (user.getEmail().equals(userEmail)) {
      throw new BadRequestException("Vous ne pouvez pas supprimer votre propre compte");
    }

    userRepository.delete(user);
  }

  private User findUserByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé : " + email));
  }

  private Role parseRole(String roleName) {
    try {
      return Role.valueOf(roleName);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Rôle invalide : " + roleName + ". Valeurs acceptées : ADMIN, EMPLOYEE");
    }
  }
}
